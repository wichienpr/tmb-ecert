import { Ex1 } from "./ex1.model";
import * as Ex1Actions from "./ex1.actions";

const initialState: Ex1 = {
    id: 1,
    name: "ex1",
    value: "125"
};

export function ex1Reducer(state: Ex1[] = [initialState], action: Ex1Actions.Actions): Ex1[] {
    switch (action.type) {

        case Ex1Actions.ADD:
            return [...state, action.payload];

        case Ex1Actions.UPDATE:
            const { index, data } = action.payload;
            state[index] = Object.assign({}, data);
            return state;

        case Ex1Actions.DELETE:
            state.splice(action.payload, 1);
            return state;

        default:
            return state;
            
    }
}