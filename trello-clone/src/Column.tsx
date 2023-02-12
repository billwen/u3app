import React, {useRef} from "react";
import {ColumnContainer, ColumnTitle, CardContainer} from "./styles";
import {Card} from "./Card";
import {AddNewItem} from "./AddNewItem";
import {useAppState} from "./state/AppStateContext";
import {addTask, moveList, moveTask, setDraggedItem} from "./state/actions";
import {useDrop} from "react-dnd";
import {throttle} from "throttle-debounce-ts";
import {useItemDrag} from "./utils/useItemDrag";
import {isHidden} from "./utils/isHidden";

type ColumnProps = {
    text: string;
    id: string;

    isPreview?: boolean;
}

export const Column = ({ text,id, isPreview }: ColumnProps) => {
    const ref = useRef<HTMLDivElement>(null);
    const { drag } = useItemDrag({type: "COLUMN", id, text});
    const { draggedItem, getTasksByListId, dispatch } = useAppState();

    const [, drop] = useDrop({
        accept: ["COLUMN", "CARD"],
        hover: throttle(200, () => {
            if (!draggedItem) {
                return;
            }
            if (draggedItem.type === "COLUMN") {
                if (draggedItem.id === id) {
                    return;
                }

                dispatch(moveList(draggedItem.id, id));
            } else {
                if (draggedItem.columnId === id) {
                    return;
                }
                if (tasks.length) {
                    return;
                }

                dispatch(moveTask(draggedItem.id, null, draggedItem.columnId, id));
                dispatch(setDraggedItem({...draggedItem, columnId: id}));
            }
        })
    });

    const tasks = getTasksByListId(id);

    drag(drop(ref));

    return (
        <ColumnContainer ref={ref} isHidden={isHidden(draggedItem, "COLUMN",id)} isPreview={isPreview}>
            <ColumnTitle>{text}</ColumnTitle>
            {
                tasks.map( (task) => (
                    <Card columnId={id} text={task.text} key={task.id} id={task.id} />
                ))
            }

            <AddNewItem toggleButtonText="+ Add another card" onAdd={(text) => dispatch(addTask(text, id))} dark/>
        </ColumnContainer>
    );
}