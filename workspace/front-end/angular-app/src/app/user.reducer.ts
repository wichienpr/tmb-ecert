import { Action } from '@ngrx/store';
import { UserDetail } from './user.model';
import * as UserActions from './user.action';

// Initial
const INIT_USER_DETAIL: UserDetail = {
    roles: ['ADMIN'],
    username : 'admin',
    firstName : 'Administrator',
    lastName : 'TMB'
};

// Reducers
export function userReducer(state: UserDetail=INIT_USER_DETAIL, action: UserActions.Actions) {
    switch (action.type) {
        case UserActions.GET:
            return state;
        case UserActions.UPDATE:
            return state = action.payload; 
        default:
            return state;
    }
}
