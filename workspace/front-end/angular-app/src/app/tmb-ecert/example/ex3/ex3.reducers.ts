import * as Ex3Actions from './ex3.actions';
import { Ex3 } from './ex3.model';

const initialState: Ex3[] = [
    { name: "AAA", no: "123" },
    { name: "KKK", no: "111" },
    { name: "KAA", no: "222" },
];

export function ex3Reducer(state: Ex3[] = initialState, action: Ex3Actions.Actions): Ex3[] {
    switch (action.type) {
        case Ex3Actions.SEARCH: // Instead Query Backend and Return State
            let data = initialState.slice(0);
            const { name, no } = action.payload;
            return data.filter(obj => {
                if (name || no) {
                    return obj.name.search(name) !== -1 && obj.no.search(no) !== -1;
                } else {
                    return true;
                }
            });
        default:
            return state;
    }
}