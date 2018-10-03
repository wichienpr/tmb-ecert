import { Action } from '@ngrx/store';
import { UserDetail } from './user.model';
import * as UserActions from './user.action';
import { ROLES } from 'constants/';

// Initial
const INIT_USER_DETAIL: UserDetail = {
    roles: [],
    userId: "",
    username: "",
    firstName: "",
    lastName: "",
    auths : [],
};

// Reducers
export function userReducer(state: UserDetail = INIT_USER_DETAIL, action: UserActions.Actions) {
    switch (action.type) {
        case UserActions.GET:
            return state;
        case UserActions.RESET:
            return state = INIT_USER_DETAIL;
        case UserActions.UPDATE:
            return state = action.payload;
        default:
            return state;
    }
}
