import { Action } from '@ngrx/store';


export const INCREMENT = 'INCREMENT';
export const DECREMENT = 'DECREMENT';
export const RESET = 'RESET';

export interface Nrq00000Form{
    username? : string
}

export function nrq00000Reducer(state: Nrq00000Form = {username : 'Feature[nrg000100]'}, action: Action) {
  switch (action.type) {
    case INCREMENT:
      return state;   
    default:
      return state;
  }
}