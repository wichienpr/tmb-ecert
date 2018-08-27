


export var TextDateTH = {
  days: ["อา", "จ", "อ", "พ", "พฤ", "ศ", "ส"],
  months: [
    "มกราคม",
    "กุมภาพันธ์",
    "มีนาคม",
    "เมษายน",
    "พฤษภาคม",
    "มิถุนายน",
    "กรกฎาคม",
    "สิงหาคม",
    "กันยายน",
    "ตุลาคม",
    "พฤศจิกายน",
    "ธันวาคม"
  ],
  monthsShort: [
    "ม.ค.",
    "ก.พ.",
    "มี.ค.",
    "เม.ย.",
    "พ.ค.",
    "มิ.ย.",
    "ก.ค.",
    "ส.ค.",
    "ก.ย.",
    "ต.ค.",
    "พ.ย.",
    "ธ.ค."
  ],
  today: "วันนี้",
  now: "เดี๋ยวนี้",
  am: "ก่อนบ่าย",
  pm: "หลังบ่าย"
};

export var digit = number => {
  return (number < 10 ? "0" : "") + number;
};
// formatter("ป")
export var formatter = (what: string = "") => {
  switch (what) {
    case "เวลา":
      return {
        time: function (date, settings) {
          if (!date) return "";
          var now = date,
            h = now.getHours(),
            m = now.getMinutes(),
            s = now.getSeconds();
          return digit(h) + ':' + digit(m); // + ':' + digit(s);
        }
      };
    case "ดป":
      return {
        header: function (date, mode, settings) {
          let _year = toDateLocale(date)[0].split("/")[2];
          return _year;
        },
        date: function (date, settings) {
          if (!date) return "";
          let _year = toDateLocale(date)[0].split("/")[2];
          let _month = date.getMonth();
          return TextDateTH.months[_month] + " " + _year;
        }
      };
      case "ป":
      return {
        cell: function (cell, date, cellOptions) {
          let _year = toDateLocale(date)[0].split("/")[2];
          cell[0].innerHTML = _year;
        },
        header: function (date, mode, settings) {
          let _year = toDateLocale(date)[0].split("/")[2];
          return _year;
        },
        date: function (date, settings) {
          let _year = toDateLocale(date)[0].split("/")[2];
          return _year;
        }
      };
    case "วดป":
      return {
        header: function (date, mode, settings) {
          let month = date.getMonth();
          let _year = toDateLocale(date)[0].split("/")[2];
          return TextDateTH.months[month] + " " + _year;
        },
        date: function (date, settings) {
          if (!date) return "";
          let day = date.getDate();
          let month = date.getMonth();
          let _year = toDateLocale(date)[0].split("/")[2];
          return digit(day) + " " + TextDateTH.months[month] + " " + _year.toString();
        }
      };
    case "วดปเวลา":
      return {
        datetime: function (date, mode, settings) {
          //return a string to show on the header for the given 'date' and 'mode'
          if (!date) return "";
          let day = date.getDate();
          let month = date.getMonth();
          let _year = toDateLocale(date)[0].split("/")[2];
          let h = date.getHours();
          let m = date.getMinutes();
          let s = date.getSeconds();
          return digit(day) + "/" + digit(month) + "/" + _year + " " + digit(h) + ":" + digit(m);
        },
      };
    case "day":
      return {
        header: function (date, mode, settings) {
          let _year = toDateLocale(date)[0].split("/")[2];
          let _month = TextDateTH.months[date.getMonth()];
          return `${_month} ${_year}`;
        },
        date: function (date, settings) {
          if (!date) return "";
          let day = date.getDate();
          return digit(day);
        }
      };
    case "month":
      return {
        header: function (date, mode, settings) {
          let _year = toDateLocale(date)[0].split("/")[2];
          return _year;
        },
        date: function (date, settings) {
          //return a string to show on the header for the given 'date' and 'mode'
          let _date = toDateLocale(date);
          let _month = TextDateTH.months[parseInt(_date[0].split("/")[1]) - 1];
          return _month;
        }
      };
    case "year":
      return {
        cell: function (cell, date, cellOptions) {
          let _year = toDateLocale(date)[0].split("/")[2];
          cell[0].innerHTML = _year;
        },
        header: function (date, mode, settings) {
          let _year = toDateLocale(date)[0].split("/")[2];
          return _year;
        },
        date: function (date, settings) {
          let _year = toDateLocale(date)[0].split("/")[2];
          return _year;
        }
      };
    case "day-month":
      return {
        header: function (date, mode, settings) {
          let _year = toDateLocale(date)[0].split("/")[2];
          let _month = TextDateTH.months[date.getMonth()];
          return `${_month} ${_year}`;
        },
        date: function (date, settings) {
          let _date = toDateLocale(date);
          let _month = TextDateTH.months[date.getMonth()];
          let _day = parseInt(_date[0].split("/")[0]);
          return `${_day} ${_month}`;
        }
      };
    case "month-year":
      return {
        header: function (date, mode, settings) {
          let _year = toDateLocale(date)[0].split("/")[2];
          return _year;
        },
        date: function (date, settings) {
          let _month = toDateLocale(date)[0].split("/")[1];
          let _year = toDateLocale(date)[0].split("/")[2];
          return `${_month}/${_year}`;
        }
      };

    default:
      return {
        header: function (date, mode, settings) {
          //return a string to show on the header for the given 'date' and 'mode'
          const year = date.getFullYear() + 543;
          const month = TextDateTH.months[date.getMonth()];
          return `${month} ${year}`;
        },
        date: function (date, settings) {
          if (!date) return "";
          let day = date.getDate();
          let month = date.getMonth() + 1;
          let year = date.getFullYear() + 543;
          return digit(day) + "/" + digit(month) + "/" + year;
        }
      };
  }
};

export var toDateLocale = (date) => {
  const _date = new Date(Date.UTC(date.getFullYear(), date.getMonth(), date.getDate()));
  return _date.toLocaleString('th-TH', { timeZone: 'UTC' }).split(" ");
}

export var ThaiFormatter = date => {
  const options = { year: "numeric", month: "long", day: "numeric" };
  return date.toLocaleDateString("th-TH", options);
};

export var stringToDate = (date) => {

  let strSpit = date.split(/[ ,]+/);
  if (strSpit.length > 1) {

    let _date = strSpit[0]; //date 
    let _time = strSpit[1]; //time

    //sub date
    let dateSpit = _date.split('/');
    let _dd = dateSpit[0];
    let _mm = dateSpit[1];
    let _yyyy = parseInt(dateSpit[2]) - 543;
    let mmddyyyytime = _mm + "/" + _dd + "/" + _yyyy + " " + _time;// mm/dd/yyyy time

    return new Date(mmddyyyytime);

  } else {

    let dateSpit = date.split('/');
    let _dd = dateSpit[0];
    let _mm = dateSpit[1];
    let _yyyy = parseInt(dateSpit[2]) - 543;
    let mmddyyyy = _mm + "/" + _dd + "/" + _yyyy;// mm/dd/yyyy time

    return new Date(mmddyyyy);
  }
};
export var fullMonth = (date) => {

    let dateSpit = date.split('/');
    let _dd = dateSpit[0];
    let _mm = dateSpit[1];
    let _yyyy = parseInt(dateSpit[2]);

    let month = TextDateTH.months[_mm-1];
    let ddfullmonthyyyy = parseInt(_dd) + " " + month + " " + _yyyy;// full month

    return ddfullmonthyyyy;
  };

export default { TextDateTH, formatter, digit, ThaiFormatter, stringToDate,fullMonth };
