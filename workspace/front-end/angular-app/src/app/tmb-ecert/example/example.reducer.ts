import { ActionReducerMap, createFeatureSelector, createSelector } from "@ngrx/store";
import * as fromEx1 from "./ex1/ex1.reducers";
import * as fromEx2 from "./ex2/ex2.reducers";
import { Ex1 } from "./ex1/ex1.model";
import { Ex2 } from "app/tmb-ecert/example/ex2/ex2.model";

// Combine States
export interface ExampleState {
    ex1: Ex1[];
    ex2: Ex2;
}

// Combine Reducers
export const reducers: ActionReducerMap<ExampleState> = {
    ex1: fromEx1.ex1Reducer,
    ex2: fromEx2.ex2Reducer
}

// Main State
export const getExamplesState = createFeatureSelector<ExampleState>('examples');

// Children State
export const getEx1State = createSelector(getExamplesState, (state: ExampleState) => state.ex1);
export const getEx2State = createSelector(getExamplesState, (state: ExampleState) => state.ex2);