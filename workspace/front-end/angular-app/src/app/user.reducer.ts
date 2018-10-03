import { Action } from '@ngrx/store';
import { UserDetail } from './user.model';
import * as UserActions from './user.action';
import { ROLES } from 'constants/';

// Initial
const INIT_USER_DETAIL: UserDetail = {
    // roles: ['ADMIN'],
    // userId: "00001",
    // username : 'admin',
    // firstName : 'TMB',
    // lastName : 'Administrator'
    roles: [ROLES.CHECKER, ROLES.MAKER, ROLES.REQUESTOR],
    userId: "42307",
    username: 'admin',
    firstName: 'สุรเดช',
    lastName: 'แสนสมบูรณ์สุข',
    auths : []
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