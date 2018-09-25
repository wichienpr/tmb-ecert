import { Action } from "@ngrx/store";
import { Ex1 } from "./ex1.model";

export const ADD = "EX1 => ADD";
export const UPDATE = "EX1 => UPDATE";
export const DELETE = "EX1 => DELETE";

export class AddEx1 implements Action {
    readonly type = ADD
    constructor(public payload: Ex1) {}
}

export class UpdateEx1 implements Action {
    readonly type = UPDATE
    constructor(public payload: { index: number , data: Ex1 }) {}
}

export class DeleteEx1 implements Action {
    readonly type = DELETE
    constructor(public payload: number) {}
}

export type Actions = AddEx1 | UpdateEx1 | DeleteEx1