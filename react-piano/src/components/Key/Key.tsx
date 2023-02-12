import {NoteType} from "../../domain/note";
import {FC} from "react";
import clsx from "clsx";
import styles from "./Key.module.css";

type KeyProps = {
    type: NoteType;
    label: string;
    disable?: boolean;
}

export const Key: FC<KeyProps> = (props) => {
    const { type, label, ...rest } = props;

    return (
        <button className={clsx(styles.key, styles[type])} type="button" {...rest}>
            {label}
        </button>
    );
}