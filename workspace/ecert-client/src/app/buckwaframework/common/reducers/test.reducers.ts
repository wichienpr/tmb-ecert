import { Action } from '@ngrx/store'
import { Test } from 'models/index'
import { TestActions } from 'actions/index'

const initialState: Test = {
    id: 1,
    name: "ค่าเริ่มต้น"
};

export function reducer(state: Test[] = [initialState], action: TestActions.Actions) { // [initialState]
    switch (action.type) {
        case TestActions.ADD_TEST:
            return [...state, action.payload];
        case TestActions.DEL_TEST:
            state.splice(action.payload, 1);
            return state;
        case TestActions.EDT_TEST:
            state[action.payload.index] = Object.assign({}, action.payload.data)
            return state;
        default:
            return state;
    }
}