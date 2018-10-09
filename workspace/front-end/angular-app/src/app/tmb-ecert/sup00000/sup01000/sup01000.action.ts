import { Action } from "@ngrx/store";
import { Sup01000 } from "app/tmb-ecert/sup00000/sup01000/sup01000.model";

export const UPDATE   = 'Role => UPDATE';
export const CLEAR    = 'Role => CLEAR';



export class UpdateRole implements Action {
    readonly type = UPDATE
    constructor(public payload: Sup01000) {}
}
export class ClearRole implements Action {
    readonly type = CLEAR
    constructor() {}
}
export type Actions =  UpdateRole | ClearRole