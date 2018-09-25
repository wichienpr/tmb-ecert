import { ActionReducerMap, createFeatureSelector, createSelector } from "@ngrx/store";
import * as fromEx1 from "./ex1/ex1.reducers";
import { Ex1 } from "./ex1/ex1.model";

export interface ExampleState {
    ex1: Ex1[];
}

// reducers
export const reducers: ActionReducerMap<ExampleState> = {
    ex1: fromEx1.ex1Reducer
}

// Main State
export const getExamplesState = createFeatureSelector<ExampleState>('examples');

// Children State
export const getEx1State = createSelector(getExamplesState, (state: ExampleState) => state.ex1);