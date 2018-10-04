import * as EcerDashBoard_Actions from "app/dash-board.action";

export interface EcerDashBoard {
    newReq: number,
    paying: number,
    reject: number,
    cancle: number,
    paywaiting: number,
    payapprove: number,
    payreject: number,
    payfail: number,
    waituploadcert: number,
    complete: number,
    keyinReq: number,
}

const initdashBord: EcerDashBoard = {
    newReq: 0,
    paying: 0,
    reject: 0,
    cancle: 0,
    paywaiting: 0,
    payapprove: 0,
    payreject: 0,
    payfail: 0,
    waituploadcert: 0,
    complete: 0,
    keyinReq: 0
};
// Reducers
export function ecerdashBoardReducer(state: EcerDashBoard = initdashBord, action: EcerDashBoard_Actions.Actions) {
    switch (action.type) {
        case EcerDashBoard_Actions.UPDATE:
            return action.payload;
        default:
            return state;
    }
}