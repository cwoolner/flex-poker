import * as types from '../constants/ActionTypes'

export const initOpenGameTabs = openGameTabs => ({ type: types.INIT_OPEN_GAME_TABS, openGameTabs });

export const updateOpenGameTabs = openGameTabs => ({ type: types.UPDATE_OPEN_GAME_TABS, openGameTabs })

export const updateOpenGameList = openGameList => ({ type: types.UPDATE_OPEN_GAME_LIST, openGameList });
