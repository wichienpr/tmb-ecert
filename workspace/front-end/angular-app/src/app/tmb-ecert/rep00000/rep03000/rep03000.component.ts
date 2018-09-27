import { Component, OnInit } from '@angular/core';
declare var $: any;
@Component({
  selector: 'app-rep03000',
  templateUrl: './rep03000.component.html',
  styleUrls: ['./rep03000.component.css']
})
export class Rep03000Component implements OnInit {
  showData: boolean = false; 
  constructor() { }

  ngOnInit() {
    $("#calendar1").calendar({    
      maxDate: new Date(),
      type: "month"
    });

  }
  ngAfterViewInit() {
    $("#table").DataTable();
  }
  

  searchData(): void {
    this.showData = true;
    setTimeout(() => {
      $("#table").DataTable({
        scrollX: true,
        searching :false,
        "columnDefs": [{
          "targets": 1,
          "orderable": false
        },{
          "targets": 2,
          "orderable": false
        },{
          "targets": 5,
          "orderable": false
        },{
          "targets": 6,
          "orderable": false
        },{
          "targets": 7,
          "orderable": false
        },{
          "targets": 8,
          "orderable": false
        },{
          "targets": 9,
          "orderable": false
        }]
      });
    }, 200);
  }
  
  
  clearData(): void {
    this.showData = false;
  }

}
