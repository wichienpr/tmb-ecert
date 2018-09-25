import { ActionReducer, Action } from '@ngrx/store';

export interface UserDetail {
    roles: string[]
    username: string
    firstName: string
    lastName: string
}
export const ADD = 'ADD';

const INIT_USER_DETAIL: UserDetail = {
    roles: ['ADMIN'],
    username : 'top',
    firstName : 'tee',
    lastName : 't'

};

export function userReducer(state: UserDetail=INIT_USER_DETAIL, action: Action) {
    switch (action.type) {
        case ADD:
            return state;

        default:
            return state;
    }
}
