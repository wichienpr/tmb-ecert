import { Injectable } from '@angular/core'
import { Action } from '@ngrx/store'
import { Test } from 'models/index'

export const ADD_TEST = '[TEST] Add'
export const EDT_TEST = '[TEST] Edit'
export const DEL_TEST = '[TEST] Delete'

export class AddTest implements Action {
    readonly type = ADD_TEST
    constructor(public payload: Test) {}
}

export class EdtTest implements Action {
    readonly type = EDT_TEST
    constructor(public payload: any) {}
}

export class DelTest implements Action {
    readonly type = DEL_TEST
    constructor(public payload: number) {}
}

export type Actions = AddTest | DelTest | EdtTest