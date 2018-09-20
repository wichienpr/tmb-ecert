import { Component, OnInit, ViewChild } from "@angular/core";
import { Observable } from "rxjs";
import { Test } from "models/index";
import { AppState } from "stores/app.state";
import { Store } from "@ngrx/store";
import { TestActions } from "actions/index";
import { FormControl } from "@angular/forms";

@Component({
    selector: 'app-test',
    templateUrl: './test.component.html',
    styleUrls: ['./test.component.css']
})
export class TestComponent implements OnInit {

    id: number;
    index: number;
    name: string;

    selected: string = 'add';

    tests: Observable<Test[]>;

    constructor(private store: Store<AppState>) {
        this.tests = store.select('test')
    }

    add(name: string) {
        this.store.dispatch(new TestActions.AddTest({ id: Math.round((Math.random()*1000)), name: name }))
        this.name = ''
        this.selected = 'add'
    }

    del(index: number) {
        this.store.dispatch(new TestActions.DelTest(index))
    }

    edt(index: number) {
        this.tests.subscribe(data => {
            this.selected = 'update'
            this.index=index
            this.id=data[index].id
            this.name=data[index].name
        });
    }

    upd(index: number, id: number, name: string) {
        const data: Test = {
            id: id,
            name: name
        }
        this.store.dispatch(new TestActions.EdtTest({ index: index, data: data }))
        this.selected = 'add'
        this.name = ""
    }

    ngOnInit() {}

}