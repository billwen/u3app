import {Action} from "./actions";
import {nanoid} from "nanoid";
import {findItemIndexById, moveItem} from "../utils/arrayUtils";
import {DragItem} from "../DragItem";
import {AppState} from "./AppStateContext";

export type Task = {
    id: string;
    text: string;
}

export type List = {
    id: string;
    text: string;
    tasks: Task[];
}

export const appStateReducer = (draft: AppState, action: Action): AppState | void => {
    switch (action.type) {
        case "ADD_LIST":
            draft.lists.push({
                id: nanoid(),
                text: action.payload,
                tasks: []
            });
            break;

        case "ADD_TASK":
            const { text, listId } = action.payload;
            const targetAddListIndex = findItemIndexById(draft.lists, listId);

            draft.lists[targetAddListIndex].tasks.push({
                id: nanoid(),
                text
            });
            break;

        case "MOVE_LIST":
            const { draggedId, hoverId } = action.payload;
            const dragIndex = findItemIndexById(draft.lists, draggedId);
            const hoverIndex = findItemIndexById(draft.lists, hoverId);
            draft.lists = moveItem(draft.lists, dragIndex, hoverIndex);
            break;

        case "SET_DRAGGED_ITEM":
            draft.draggedItem = action.payload;
            break;

        case "MOVE_TASK":
            const { draggedItemId, hoveredItemId, sourceColumnId, targetColumnId } = action.payload;
            const sourceListIndex = findItemIndexById(draft.lists, sourceColumnId);
            const targetMoveListIndex = findItemIndexById(draft.lists, targetColumnId);
            const dragMoveIndex = findItemIndexById(draft.lists[sourceListIndex].tasks, draggedItemId);
            const hoverMoveIndex = hoveredItemId ? findItemIndexById(draft.lists[targetMoveListIndex].tasks, hoveredItemId) : 0;
            const item = draft.lists[sourceListIndex].tasks[dragMoveIndex];
            // Remove the task from the source list
            draft.lists[sourceListIndex].tasks.splice(dragMoveIndex, 1);

            // Add the task to the target list
            draft.lists[targetMoveListIndex].tasks.splice(hoverMoveIndex, 0, item);

            break;
        default:
            throw new Error();
    }
}