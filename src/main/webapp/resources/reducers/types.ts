import { List, Map } from "immutable";

export interface Card {
    id: number;
}

export interface PocketCards {
    handId: string;
    cardId1: number;
    cardId2: number;
}

export interface Pot {
    seats: Set<string>;
    amount: number;
    isOpen: boolean;
    winners: Set<string>;
}

export interface Seat {
    position: number;
    name: string;
    chipsInBack: number;
    chipsInFront: number;
    isStillInHand: boolean
    raiseTo: number;
    callAmount: number;
    isButton: boolean;
    isSmallBlind: boolean;
    isBigBlind: boolean;
    isActionOn: boolean;
}

export interface Table {
    id: string;
    version: number;
    seats: Seat[];
    totalPot: number;
    pots: Pot[];
    visibleCommonCards: Card[];
    currentHandMinRaiseToAmount: number;
    currentHandId: string;
}

export interface OpenTableForUser {
    gameId: string;
    tableId: string;
}

export interface AppState {
    openGameTabs: [];
    openGameList: [];
    showJoinGameModal: boolean;
    joinGameId: string;
    showCreateGameModal: boolean;
    activeChatStream: { gameId: string, tableId: string };
    chatMessages: {
        lobbyMessages: List<string>;
        gameMessages: Map<string, List<string>>;
        tableMessages: Map<string, List<string>>;
    };
    activeTable: { gameId: string, tableId: string };
    tables: Map<string, Map<string, Table>>;
    actionOnTicks: Map<string, Map<any, any>>;
    pocketCards: Map<string, PocketCards>;
    redirectUrl: string;
}

export const DEFAULT_TABLE: Table = {
    id: null,
    version: 0,
    seats: [],
    totalPot: 0,
    pots: [],
    visibleCommonCards: [],
    currentHandMinRaiseToAmount: null,
    currentHandId: null,
  }

export const DEFAULT_GAME = Map<string, Table>()

export const DEFAULT_POCKET_CARDS = {
  cardId1: null,
  cardId2: null,
}

export const INITITAL_APP_STATE: AppState = {
    openGameTabs: [],
    openGameList: [],
    showJoinGameModal: false,
    joinGameId: null,
    showCreateGameModal: false,
    activeChatStream: { gameId: null, tableId: null },
    chatMessages: {
        lobbyMessages: List<string>(),
        gameMessages: Map<string, List<string>>(),
        tableMessages: Map<string, List<string>>()
    },
    activeTable: { gameId: null, tableId: null },
    tables: Map<string, Map<string, Table>>(),
    actionOnTicks: Map<string, Map<any, any>>(),
    pocketCards: Map<string, PocketCards>(),
    redirectUrl: null,
}
