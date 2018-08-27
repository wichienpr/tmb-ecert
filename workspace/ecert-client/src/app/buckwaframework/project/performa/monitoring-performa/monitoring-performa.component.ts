import { Component, OnInit } from '@angular/core';
import { TextDateTH } from '../../../common/\u0E49helper/datepicker';
declare var $: any;
@Component({
  selector: 'app-monitoring-performa',
  templateUrl: './monitoring-performa.component.html',
  styleUrls: ['./monitoring-performa.component.css']
})
export class MonitoringPerformaComponent implements OnInit {
  
  constructor() {
    
  
   }

  ngOnInit() {
    $("#calendarLast").calendar({
      startCalendar: $("#calendarFront"),
      maxDate: new Date(),
      type: "month",
      text: TextDateTH,
  
    });

    $("#calendarLast").calendar({
      startCalendar: $("#calendarLast"),
      maxDate: new Date(),
      type: "month",
      text: TextDateTH,
  
    });

}

}
