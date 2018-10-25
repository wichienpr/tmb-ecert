
import * as SUP01000ACTION from "app/tmb-ecert/sup00000/sup01000/sup01000.action";
import { Sup01000 } from "app/tmb-ecert/sup00000/sup01000/sup01000.model";

// Initial
const INIT_DATA: Sup01000 = {
    roldId:null,
    status: 2,
    roleName: '',
    rolePermissionId: '',
    functionCode:'',
    
    searchRoleName:'',
    searchRoleStatus:null,
    isSearch:false
};

export function roleReducer(state: Sup01000 = INIT_DATA, action: SUP01000ACTION.Actions){
    switch (action.type) {
        case SUP01000ACTION.UPDATE:
            return  Object.assign({},action.payload);
        case SUP01000ACTION.CLEAR:
            return state = INIT_DATA;
        default:
            return state;
    }

}

