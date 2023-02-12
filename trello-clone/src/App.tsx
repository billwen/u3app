import React, {useReducer} from 'react';
import logo from './logo.svg';
import './App.css';

import {AppContainer} from "./styles";
import {Column} from "./Column";
import {AddNewItem} from "./AddNewItem";
import {useAppState} from "./state/AppStateContext";
import {addList} from "./state/actions";
import {CustomDragLayer} from "./CustomDragLayer";

// interface State {
//     count: number;
// }
//
// type Action = { type: "increment" } | { type: "decrement"};
//
// const counterReducer = (state: State, action: Action) => {
//   switch (action.type) {
//       case "increment":
//           return { count: state.count + 1 };
//
//       case "decrement":
//           return { count: state.count - 1 };
//
//       default:
//           throw new Error();
//   }
// };

function App() {
    const { lists, dispatch } = useAppState();

    // const [state, dispatch] = useReducer(counterReducer, {count: 0 });

  return (
    <AppContainer>
        <CustomDragLayer />
        {
            lists.map( (list) => (
                <Column text={list.text} key={list.id} id={list.id} />
            ))
        }

        <AddNewItem toggleButtonText="+ Add another list" onAdd={(text) => dispatch(addList(text))} />
    </AppContainer>
  );
}

export default App;
