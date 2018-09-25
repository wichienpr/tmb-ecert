import { Ex2 } from './ex2.model';
import * as Ex2Actions from './ex2.actions';

const initialState: Ex2 = {
    name: "COUNT",
    count: 0
};

export function ex2Reducer(state: Ex2 = initialState, action: Ex2Actions.Actions): Ex2 {
    switch (action.type) {
        case Ex2Actions.INCREASE:
            ++state.count;
            return state;
        case Ex2Actions.DECREASE:
            --state.count;
            return state;
        case Ex2Actions.RESET:
            state = Object.assign({}, initialState);
            return state;
        default:
            return state;
    }
}