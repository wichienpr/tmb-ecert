import { Action } from "@ngrx/store";
import { sup03000 } from "app/tmb-ecert/sup00000/sup03000/sup03000.model";

export const GET    = 'Email => GET';
export const UPDATE   = 'Email => UPDATE';
export const CLEAR    = 'Email => CLEAR';


export class UpdateEmail implements Action {
    readonly type = UPDATE
    constructor(public payload: sup03000) {}
}
export class ClearEmail implements Action {
    readonly type = CLEAR
    constructor() {}
}
export type Actions =  UpdateEmail | ClearEmail