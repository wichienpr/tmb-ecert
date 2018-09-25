import { ActionReducerMap, createFeatureSelector, createSelector } from "@ngrx/store";

// Reducers
import * as fromEx1 from "./ex1/ex1.reducers";
import * as fromEx2 from "./ex2/ex2.reducers";
import * as fromEx3 from "./ex3/ex3.reducers";

// Models
import { Ex1 } from "./ex1/ex1.model";
import { Ex2 } from "./ex2/ex2.model";
import { Ex3 } from "./ex3/ex3.model";

// Combine States
export interface ExampleState {
    ex1: Ex1[];
    ex2: Ex2;
    ex3: Ex3[];
}

// Combine Reducers
export const reducers: ActionReducerMap<ExampleState> = {
    ex1: fromEx1.ex1Reducer,
    ex2: fromEx2.ex2Reducer,
    ex3: fromEx3.ex3Reducer
}

// Main State
export const getExamplesState = createFeatureSelector<ExampleState>('examples');

// Children State
export const getEx1State = createSelector(getExamplesState, (state: ExampleState) => state.ex1);
export const getEx2State = createSelector(getExamplesState, (state: ExampleState) => state.ex2);
export const getEx3State = createSelector(getExamplesState, (state: ExampleState) => state.ex3);