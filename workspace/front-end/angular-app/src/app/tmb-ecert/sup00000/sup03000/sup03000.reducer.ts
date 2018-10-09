import { sup03000 } from "app/tmb-ecert/sup00000/sup03000/sup03000.model";
import * as SUP03000ACTION from "app/tmb-ecert/sup00000/sup03000/sup03000.action";

// Initial
const INIT_DATA: sup03000 = {
    emailConfigId: null,
    emailNameTemplate:'',
    emailStatus:null,
    name: '',
    status: null,
    stateSearch:false,
    
};

export function emailReducer(state: sup03000 = INIT_DATA, action: SUP03000ACTION.Actions){
    switch (action.type) {
        case SUP03000ACTION.UPDATE:
            return  Object.assign({},action.payload);
        case SUP03000ACTION.CLEAR:
            return state = INIT_DATA;
        default:
            return state;
    }

}

