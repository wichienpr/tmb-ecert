import { Action } from "@ngrx/store";
import { EcerDashBoard } from "app/dash-board.reducer";

export const UPDATE    = 'DashBord => UPDATE';

export class Update implements Action {
    readonly type = UPDATE
    constructor(public payload: EcerDashBoard) {}
}

export type Actions = Update;