import React, {createContext, Dispatch, FC, PropsWithChildren, useContext, useEffect} from "react";
import {Action} from "./actions";
import {useImmerReducer} from "use-immer";
import {appStateReducer} from "./appStateReducer";
import {DragItem} from "../DragItem";
import {save} from "../api";
import {withInitialState} from "../withInitialState";

type Task = {
    id: string;
    text: string;
}

type List = {
    id: string;
    text: string;
    tasks: Task[];
}

export type AppState = {
    lists: List[];
    draggedItem: DragItem | null
}

const appData: AppState = {
    lists: [
        {
            id: "0",
            text: "To Do",
            tasks: [
                {
                    id: "c0",
                    text: "Generate app scaffold"
                }
            ]
        },
        {
            id: "1",
            text: "In Progress",
            tasks: [
                {
                    id: "c2",
                    text: "Learn Typescript"
                }
            ]
        },
        {
            id: "2",
            text: "Done",
            tasks: [
                {
                    id: "c3",
                    text: "Begin to use static typing"
                }
            ]
        }
    ],
    draggedItem: null
}

type AppStateContextProps = {
    draggedItem: DragItem | null;
    lists: List[];
    getTasksByListId(id: string): Task[];
    dispatch: Dispatch<Action>;
}

const AppStateContext = createContext<AppStateContextProps>( {} as AppStateContextProps);

interface AppStateProviderProps {
    initialState: AppState;
}
export const AppStateProvider = ({ children }: PropsWithChildren<AppStateProviderProps>) => {

/*    const [state, dispatch] = useImmerReducer(appStateReducer, appData);

    const { draggedItem, lists } = state;

    const getTasksByListId = (id: string) => {
        return lists.find( (list) => list.id === id)?.tasks || [];
    }

    useEffect( () => {
        save(state);
    }, [state]);

    return (
        <AppStateContext.Provider value={{ draggedItem, lists, getTasksByListId, dispatch}}>
            {children}
        </AppStateContext.Provider>
    );*/

    withInitialState<PropsWithChildren<AppStateProviderProps>>(
        ({children, initialState}) => {
            const [state, dispatch] = useImmerReducer(appStateReducer, initialState);

            useEffect( () => {
                save(state);
            }, [state]);

            const { draggedItem, lists } = state;
            const getTasksByListId = (id: string) => {
                return lists.find( (list) => list.id === id)?.tasks || [];
            }

            return (
                <AppStateContext.Provider value={{draggedItem, lists, getTasksByListId, dispatch}}>
                    {children}
                </AppStateContext.Provider>
            );
        }
    );
}

export const useAppState = () => {
    return useContext(AppStateContext);
}