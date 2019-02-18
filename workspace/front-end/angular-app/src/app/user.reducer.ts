import { Action } from '@ngrx/store';
import { UserDetail } from './user.model';
import * as UserActions from './user.action';
import { ROLES } from 'constants/';

// Initial
// const INIT_USER_DETAIL: UserDetail = {
//     auths: ["0000200", "0000300", "0000301", "0000400", "0000401", "0000402", "0000403", "0000404", "0000405", "0000406", "0000407", "0000408", "0000409", "0000410", "0000500", "0000600", "0000700", "0000701", "0000800", "0000801", "0000900", "0000901", "0001100", "0001101", "0001200", "0001201", "0001300", "0001301", "0001302", "0001303", "0001304", "0001400", "0001401", "0001500", "0001501"],
//     firstName: "ผู้ดูแลระบบ",
//     lastName: "ธนาคารทหารไทย",
//     roles: [ROLES.MAKER],
//     userId: "0001",
//     username: "admin",
//     segment: "20001"
// };
const INIT_USER_DETAIL: UserDetail = {
    roles: [],
    userId: "",
    username: "",
    firstName: "",
    lastName: "",
    auths: [],
    segment: "",
    department:""
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
