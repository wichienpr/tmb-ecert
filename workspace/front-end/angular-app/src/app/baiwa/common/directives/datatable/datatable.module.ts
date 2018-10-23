import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { DatatableDirective } from "./datatable.directive";
import { DatatableSearchComponent } from "./datatable-search.component";
import { DatatableFooterComponent } from "./datatable-footer.component";
import { NgSortDirective } from "./ngsort.directive";

@NgModule({
    imports: [CommonModule],
    declarations: [DatatableDirective, DatatableSearchComponent, DatatableFooterComponent, NgSortDirective],
    exports: [DatatableDirective, DatatableSearchComponent, DatatableFooterComponent, NgSortDirective]
})
export class DatatableModule { }