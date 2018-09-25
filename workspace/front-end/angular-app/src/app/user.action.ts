import { Action } from '@ngrx/store';
import { UserDetail } from "./user.model";

// Actions
export const GET    = 'USER => GET';
export const UPDATE = 'USER => UPDATE';

export class GetUser implements Action {
    readonly type = GET
    constructor(public payload: UserDetail) {}
}

export class UpdateUser implements Action {
    readonly type = UPDATE
    constructor(public payload: UserDetail) {}
}

export type Actions = GetUser | UpdateUser