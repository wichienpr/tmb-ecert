import { Ex2 } from "./ex2.model";
import { Action } from "@ngrx/store";

export const INCREASE = "EX2 => INCREASE";
export const DECREASE = "EX2 => DECREASE";
export const RESET = "EX2 => RESET";

export class InCreseEx2 implements Action {
    readonly type = INCREASE
    constructor(public payload: number) { }
}

export class DeCreseEx2 implements Action {
    readonly type = DECREASE
    constructor(public payload: number) { }
}

export class ResetEx2 implements Action {
    readonly type = RESET
    constructor(public payload: number) {}
}

export type Actions = InCreseEx2 | DeCreseEx2 | ResetEx2;