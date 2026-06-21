# Flex Poker Project Rules

This file contains persistent instructions for working in the Flex Poker codebase (Kotlin/Spring Boot backend + React/TypeScript frontend).

## Architecture (Critical)

Flex Poker uses a lightweight custom **CQRS + Event Sourcing** architecture with strict separation:

- **Command side** (write path): 
  - Aggregates live in `<domain>.command.aggregate` (e.g. `game.command.aggregate`, `table.command.aggregate`).
  - State is represented as immutable data classes (usually `GameState`, `TableState`, `HandState`).
  - **Event producers** are pure top-level `fun` functions in `eventproducers/` subpackages. They take current state (or inputs) and return `List<Event>`.
  - State is reconstructed with pure `applyEvents(state?, events)` / `applyEvent` functions.
  - Command handlers (`@Component` implementing `CommandHandler<T>`) are `@Async`, call an event producer, save events via the domain's `EventRepository`, then publish via `EventPublisher`.
- **Query side** (read models):
  - Event subscribers (`<domain>.query.eventsubscribers`) listen to events and update read-side repositories.
  - DTOs in `<domain>.query.dto`.
- **Process Managers** (in root `processmanagers/` package): Handle orchestration, cross-aggregate logic, and timers (e.g. `CreateInitialTablesForGameProcessManager`, blinds countdowns, action-on timers). They implement `ProcessManager<Event>` and send commands via `CommandSender`.
- **Push notifications**: Separate handlers in `pushnotificationhandlers/` react to events and push updates to clients (via WebSocket).
- **Framework interfaces** (`framework/command`, `framework/event`, `framework/processmanager`): Keep usage consistent (CommandHandler/CommandSender, EventHandler/EventPublisher, ProcessManager).

**Strict rules enforced by ArchUnit tests** (never break these):
- Domains are isolated: `chat`, `login`, `signup`, `game`, `table` do not depend on each other (except allowed cross-domain flows through commands/events).
- Command side is completely insulated from query side: `*.command.*` packages must not depend on `*.query.*` (same domain or otherwise).
- Controllers, Repositories, and Services must be properly annotated and use constructor injection only (no field injection).
- See `src/test/kotlin/com/flexpoker/archtest/` for the exact rules.

When adding features:
- Most gameplay changes require updates on **both** command side (event production + handlers) **and** query side (subscribers + read models).
- Time-based or multi-aggregate behavior usually belongs in a Process Manager.
- New repositories always need: interface + `InMemory*` impl + `Redis*` impl.

## Persistence & Spring Profiles

- Default experience is fully in-memory (no external DB required).
- Repositories follow a strict pattern per "slice":
  - `<Name>Repository` (interface)
  - `InMemory<Name>Repository` annotated `@Profile(ProfileNames.DEFAULT)` or the specific `*-inmemory` profile
  - `Redis<Name>Repository` annotated `@Profile("redis")` or the specific `*-redis` profile
- Fine-grained profiles exist (see `config/ProfileNames.kt`): `game-command-inmemory`/`game-command-redis`, `table-query-redis`, etc.
- You can mix profiles at startup, e.g.:
  `mvn -Dspring-boot.run.profiles=game-command-redis,table-command-inmemory,... spring-boot:run`
- Redis profile requires a running Redis instance. In-memory is preferred for local dev and most tests.

## Build, Test, Run Commands

**Frontend build** (required before running the app):
- `npm install`
- `npm run prod` (production build)
- `npm run dev` (watch mode for development)

**Backend** (JDK 25 required):
- `mvn spring-boot:run` — default (all in-memory)
- `mvn -Dspring-boot.run.profiles=redis spring-boot:run` — full Redis
- Docker: `docker build -t flex-poker:1.0-SNAPSHOT .` then `docker run -d -p 8080:8080 flex-poker:1.0-SNAPSHOT`

**Testing**:
- `mvn test` — runs everything (unit + arch + in-memory repo tests). Redis repo tests require Docker/Testcontainers.
- Fast unit tests only: `mvn test -Dgroups=unit`
- Exclude Redis tests: `mvn test -DexcludedGroups=repository-redis`
- Arch tests only: `mvn test -Dgroups=archunit`
- Mutation testing (PIT): `mvn test-compile org.pitest:pitest-maven:mutationCoverage`
- Test classes are annotated for selection:
  - `@UnitTestClass` → tag "unit"
  - `@ArchUnitTestClass` → tag "archunit"
  - `@InMemoryTestClass` → tag "repository-inmemory" (default profile)
  - `@RedisTestClass` → tag "repository-redis" (redis profile + Testcontainers)

**Playwright E2E tests** (`e2e/` directory):
- Start the full stack first: `npm run prod` then `mvn spring-boot:run` (server must be running on 8080; no automatic `webServer`).
- Run with `npm run e2e` or `npm run e2e:ui`.
- Organized into focused spec files: `smoke.spec.ts`, `lobby.spec.ts`, `table.spec.ts`.
- Shared helpers in `e2e/helpers/` (e.g. `loginAs`, `createGame`, `joinGame`, `openJoinDialogForGame`).
- Always use unique game names (incorporate `Date.now()`) because the backend shares in-memory state.
- Multi-player / table tests use `browser.newContext()` + separate `Page` instances.
- Prefer `getByRole` / `getByLabel` / `getByText` + `.toBeVisible()` waits to handle async WebSocket updates.

Default login users (created at startup in both in-memory and Redis impls): `player1/player1`, `player2/player2`, etc.

## Coding Conventions

**Kotlin / Backend**:
- Use persistent collections from `org.pcollections` (`PSet`, `PMap`, `HashTreePSet`, `HashTreePMap`, plus `PVector`/`TreePVector` via `PCollectionExtensions.kt`) for all command-side aggregate state.
- Event producers and `applyEvent`/`applyEvents` functions are pure and top-level.
- Prefer constructor injection (`@Inject constructor(...)`).
- Command handlers, process managers, and many subscribers are marked `@Async`.
- Keep aggregate logic in the `aggregate/` + `eventproducers/` packages. Handlers are thin coordinators.
- Use `require(...)` for validation inside state/objects (see `BlindSchedule`, etc.).
- New events go in the domain's `events/` package as data classes implementing the domain `Event` interface.
- Follow existing package layout exactly.

**Frontend (TypeScript/React)**:
- Classic Redux (not Toolkit): plain action creator functions + single root reducer in `reducers/index.ts`.
- State uses `immutable` (v4) `Map` and `List` (see `reducers/types.ts` and reducers).
- Functional components + hooks (`useState`, `useEffect`, `useDispatch`, `useSelector`).
- React-Bootstrap for UI components.
- WebSocket push updates via `WebSocketService`, dedicated subscriber modules, and Redux actions like `tableUpdateReceived`.
- Bundling is handled by webpack (`webpack.config.js`). Run `npm run prod` (or `dev`) to produce bundles served from `src/main/resources`.
- Keep components modular under `modules/` (lobby, table, home, etc.).

**General**:
- No special formatter/linter config files at repo root (follow existing code style closely).
- Web layer: Controllers live in `web/controller/`. Never import controller classes from domain code (enforced).
- Hardcoded/seed data and current limitations are documented in `README.md`.

## When Working on Features or Fixes

1. Respect all ArchUnit rules — run the arch tests early if touching packages or dependencies.
2. For any gameplay or state change: identify the event(s), the producer(s), the handler(s), the subscriber(s), and the read model updates.
3. Timers, countdowns, auto-actions, and table balancing live in Process Managers + command handlers (e.g. `ActionOnCountdownProcessManager`, `TickActionOnTimerCommandHandler`).
4. Hand evaluation and pot logic is in `table.command.aggregate` (see `HandEvaluatorService`, `PotHandler`, event producers under `hand/`).
5. Frontend state is driven by WebSocket messages; keep subscribers and reducers in sync with backend DTOs/events.
6. When adding a new repository slice, implement the full InMemory + Redis pair + update the relevant Spring profile constants if introducing a brand new named profile.
7. The UI is intentionally basic/ugly at present — focus on correctness and architecture first.

## Current Scope Notes (from README)

Core flows that work: login, signup (no email), create/join game, betting actions (call/check/fold/raise), action-on timers, blind increments, multi-table balancing.

Many features are intentionally not implemented yet (proper hand showdown UI, sounds, advanced betting UI, full winner determination display, etc.). See README "Current status" section for the full list.

## Useful Commands Quick Reference

- Run full stack locally: `npm run prod && mvn spring-boot:run`
- Fast feedback loop: `npm run dev` (in one terminal) + `mvn spring-boot:run` (in another)
- Verify architecture after changes: `mvn test -Dgroups=archunit`
- Run only fast tests: `mvn test -Dgroups=unit`

Update this file when project-wide conventions change.
