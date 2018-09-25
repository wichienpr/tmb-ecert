import { Action } from "@ngrx/store";
import { Ex3 } from "./ex3.model";

export const SEARCH = "EX3 => SEARCH";

export class SearchEx3 implements Action {
    readonly type = SEARCH
    constructor(public payload: Ex3) {}
}

export type Actions = SearchEx3;