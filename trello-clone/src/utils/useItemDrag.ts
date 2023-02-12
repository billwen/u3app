import {DragItem} from "../DragItem";
import {useAppState} from "../state/AppStateContext";
import {useDrag} from "react-dnd";
import {setDraggedItem} from "../state/actions";
import {useEffect} from "react";
import {getEmptyImage} from "react-dnd-html5-backend";

/**
 *
 * @param item
 * @Returns three values inside the array
 * [0] - CollectedProps, an object containing collected properties from the collect function.
 * If no collect function is defined, an empty object is returned.
 * [1] - DragSource Ref: A connector function for the drag source.
 * [2] - DragPreview Ref: A connector function for the drag preview.
 */
export const useItemDrag = (item: DragItem) => {
    const { dispatch } = useAppState();
    const [, drag, preview] = useDrag({
        type: item.type,
        item: () => {
            dispatch(setDraggedItem(item));
            return item;
        },
        end: () => dispatch(setDraggedItem(null))
    });

    useEffect( () => {
        preview(getEmptyImage(), { captureDraggingState: true})
    }, [preview]);

    return { drag };
}