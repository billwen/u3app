import React from "react";
import {css} from "@emotion/css";

const BookablesPageStyles = css`
  max-width: 70em;
  grid-template-columns: 1fr 3fr;
  grid-column-gap: 40px;
`;

const BookablesPage = () => {
    return (
        <main className={BookablesPageStyles}>
            <p>Bookables!</p>
        </main>
    );
};

export default BookablesPage;