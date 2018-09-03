import { Component, OnInit } from '@angular/core';
declare var $: any;
@Component({
  selector: 'app-cause-monthly',
  templateUrl: './cause-monthly.component.html',
  styleUrls: ['./cause-monthly.component.css']
})
export class CauseMonthlyComponent implements OnInit {

  constructor() { }

  ngOnInit() {

    setTimeout(() => {
      $("#table").DataTable({
         scrollX: true, 
         ordering: true, 
         searching: false,  
         "columnDefs": [{
          "targets": 10,
          "orderable": false
        }]   
           
      
      });
    }, 200);
  }

}
