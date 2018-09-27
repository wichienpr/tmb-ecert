import { Component, OnInit } from '@angular/core';
declare var $: any;
@Component({
  selector: 'app-rep02000',
  templateUrl: './rep02000.component.html',
  styleUrls: ['./rep02000.component.css']
})
export class Rep02000Component implements OnInit {

  showData: boolean = false;

  modal: string[] = ["modal"];
  constructor() { }

  ngOnInit() {
    $("#calendar1").calendar({
      maxDate: new Date(),
      type: "month"

    });
    $("#calendar2").calendar({
      maxDate: new Date(),
      type: "month"
    });


  }
  ngAfterViewInit() {

  }


  openModal(id) {
    $('#' + id).modal('show');
  }

  closeModal(id) {
    $('#' + id).modal('hide');
  }

  searchData(): void {
    this.showData = true;
    setTimeout(() => {
      $("#table").DataTable({
        scrollX: true,
        searching: false,
        ordering: true,
        paging: true,
        "columnDefs": [{
          "targets": [2, 3, 4, 5, 7, 8, 9],
          "orderable": false
        }]
      });
    });

  }

  clearData(): void {
    this.showData = false;
  }

}
