let classList = []; // danh sach lop hoc
let count = 10
let classStudent = {
    init() {
        $.ajax({
            url: `http://localhost:8080/api/class`,
            async: false,
            method: "GET",
            dataType: "json",
            success: function (data) {
                for (let i = 0; i < data.length; i++) {
                    let newClass = data[i];
                    classList.push(newClass);
                }

            },
            error: function (err) {
                console.log(err.responseJSON);
            }
        });
    }
};
let showAllStudent = []
let name = []
let nameData = []
let dob = []
let dobData = []


let display = {
    // show display
    initPage() {
        $('#right-panel').empty();
        $('#right-panel').append(`
        <header id="header" class="header">
            <div>
                <h1 style="margin-left: 42%;">LIST STUDENT</h1>
                <div class="navbar-header">
                </div>
            </div>
        </header>
        <div class="col-lg-4 col-md-4 col-sm-4 col-xs-4" style="margin-left: 1.5%">
            <select class="form-control display mb-3" id="display" onchange="display.showListStudent(0)"
                    style="margin-left: -5%;     width: 45%;">
            </select>
        </div>
        <div class="wrap col-lg-12">
             <div>
             <table class="table">
                  <thead class="thead-dark">

                    <tr class="head">
                        <th scope="col" style="width: 85px">STT</th>
                        <th scope="col" style="width: 90px">ID</th>
                        <th scope="col" style="width: 290px">Name student</th>
                        <th scope="col" style="width: 200px">Date of Birth</th>
                        <th scope="col" style="width: 170px">Gender</th>
                        <th scope="col" style="width: 250px">Phone</th>
                        <th scope="col" >Note</th>
                    </tr>
                    </thead>
                   </table>
            </div>
            <div class="scroll-table">
                <table class="table">
                    <tbody id="showListStudent">
                    </tbody>
                </table>
            </div >
   <a href="#" onclick="display.exports()"  style="margin-left: 90% " class="btn btn-primary">Export</a>
        </div>
        
    `);
        display.displayClass();
        display.showListStudent(1);
    },
    //show class
    displayClass() {
        $('.display').empty();
        for (let i = 0; i < classList.length; i++) {
            $('.display').append(`
                   <option value="${classList[i].id}">${classList[i].nameClass}</option>
        `);
        }
        $('#classList-0').click();
    },


    // show all student - class_id
    showListStudent(classID) {
        if (classID > 0) {
            $.ajax({
                url: `http://localhost:8080/api/student?class=${Number(classID)}`,
                async: false,
                method: "GET",
                dataType: "json",
                success: function (data) {
                    $('#showListStudent').empty();
                    $.each(data, function (i, v) {
                        today = new Date(v.dob)
                        let dd = today.getDate()
                        let mm = today.getMonth() + 1
                        let yyyy = today.getFullYear()
                        if (dd < 10) {
                            dd = '0' + dd
                        }
                        if (mm < 10) {
                            mm = '0' + mm;
                        }
                        let on = dd + '-' + mm + '-' + yyyy

                        $('#showListStudent').append(
                            "<tr>" +
                            "<td>" + i + "</td>" +
                            "<td>" + v.student_id + "</td>" +
                            "<td>" + v.name + "</td>" +
                            "<td>" + on + "</td>" +
                            "<td>" + v.gender + "</td>" +
                            "<td>" + v.phoneNumber + "</td>" +
                            "<td>" + v.note + "</td>" +
                            "</tr>"
                        );

                    })

                }
            })

        }
        else {
            $.ajax({
                url: `http://localhost:8080/api/student?class=${Number($('#display').val())}`,
                async: false,
                method: "GET",
                dataType: "json",
                success: function (data) {
                    
                    $('#showListStudent').empty();
                    $.each(data, function (i, v) {

                        $('#showListStudent').append(
                            "<tr>" +
                            "<td>" + i + "</td>" +
                            "<td>" + v.student_id + "</td>" +
                            "<td>" + v.name + "</td>" +
                            "<td>" + v.dob + "</td>" +
                            "<td>" + v.gender + "</td>" +
                            "<td>" + v.phoneNumber + "</td>" +
                            "<td>" + v.note + "</td>" +
                            "</tr>"
                        );

                    })

                }
            })
        }
    },
    // export file excel
    exports() {
        let studentExport = [];
        $.ajax({
            url: `http://localhost:8080/api/student?class=${Number($('#display').val())}`,
            async: false,
            method: "GET",
            dataType: "json",
            success: function (data) {
                $('#showListStudent').empty();
                $.each(data, function (i, v) {
                    $('#showListStudent').append(
                        "<tr>" +
                        "<td>" + i + "</td>" +
                        "<td>" + v.student_id + "</td>" +
                        "<td>" + v.name + "</td>" +
                        "<td>" + v.dob + "</td>" +
                        "<td>" + v.gender + "</td>" +
                        "<td>" + v.phoneNumber + "</td>" +
                        "<td>" + v.note + "</td>" +
                        "</tr>"
                    );
                })
                studentExport.push(data)
            }
        })
        $.ajax({
            url: `http://localhost:8080/api/student/export`,
            async: false,
            method: "POST",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(studentExport[0]),
            success: function () {

                if (window.confirm('Do you want to download the file?'))
                {
                    window.location.href='http://localhost:8080/api/student/download/studentList.xlsx';
                };
            },
            error: function () {
                alert('Minimum data to export is 1')
            }

        })
    }
};

let student = {
    //show add
    initPage() {
        $('#right-panel').empty();
        $('#right-panel').append(`
        <header id="header" class="header">
            <div>
                <h1 style="margin-left: 42%;">ADD Student</h1>
                <div class="navbar-header">
                </div>
            </div>
        </header>
        <div class="content">
            <div id=alert></div>
            <div style="margin-left: 7%">
                <table>
                    <tr>
                        <td>
                            <select class="form-control class-list"
                                    style="background-color: white; width: 88px;height: 35px;margin-left: -24%;">
                            </select>
                        </td>
                        <td>
                            <input type="hidden" id="id">
                            <input type="text" class="form-control" placeholder="name" id="name-back"
                                   style="height: 35px;margin-left: 13%;" required>
                        </td>
                        <td>
                            <input type="date" class="form-control" id="date-back" data-da
                                   style="height: 35px; margin-left: 45%;">
                        </td>
                        <td>
                       
                            <div style="margin-left: 175%;"> 
                            <div class="form-check " >
                              <input class="form-check-input gender-radio" type="radio" name="gender-radio" id="gender-male" value="male" checked>
                              <label class="form-check-label" for="exampleRadios1">
                               Male
                              </label>
                            </div>
                            <div class="form-check " >
                              <input class="form-check-input gender-radio" type="radio" name="gender-radio" id="gender-female" value="female">
                              <label class="form-check-label" for="exampleRadios2">
                                Female
                              </label>
                            </div>
                            </div>
                        </td>
                        <td>
                            <input type="number" id="phone-back" placeholder="Phone Number" class="form-control"
                                    style="height: 35px;margin-left: 92%;" >
                        </td>
                        <td>
                            <input type="text" class="form-control" id="note-back" placeholder="note" 
                                   style="height: 35px;margin-left: 118%;">
                        </td>
                    </tr>

                </table>
            </div>
            <div>
                <button type="button" class="btn btn-primary  btn btn-success swalDefaultSuccess"
                        style="margin-left: 87%;margin-bottom: 1%;margin-top: 1%" onclick="student.backInput()">Batch
                    Input
                </button>

            </div>
            <div class="animated fadeIn">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="table-stats order-table ov-h">
                            <table class="table ">
                                <thead>
                                <tr>
                                    <th class="serial" style="padding-left: 1.5%">
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox" value=""
                                                   id="flexCheckDefault">
                                            <label class="form-check-label" for="flexCheckDefault">
                                            </label>
                                        </div>
                                    </th>
                                    <th style="padding-left: 3%">class</th>
                                    <th style="padding-left: 5%">Name Student</th>
                                    <th style="padding-left: 8%">Dob</th>
                                    <th style="padding-left: 2%">gender</th>
                                    <th style="padding-left: 5%">Phone Number</th>
                                    <th style="padding-right: 8%">Note</th>
                                </tr>
                                </thead>
                                <tbody id="std-list">

                                </tbody>
                            </table>
                            <div class="modal-footer">
                            
                                <button type="button" class="btn btn-danger" onclick= student.confirm()  " > Delete
                 
                                </button>
                                <button type="button" class="btn btn-primary  btn btn-success swalDefaultSuccess"
                                        onclick="student.save()">Register
                                </button>

                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
        <div class="clearfix"></div>
        `);
        student.showAddListStudent();
    },


    // show add list 1-10 student
    showAddListStudent() {
        $('#std-list').empty();
        for (let i = 0; i < 10; i++) {
            $('#std-list').append(`
        <tr>
            <td class="serial">
                <div class="form-check">
                    <input class="form-check-input student-rows" id="student-checker-${i}" type="checkbox" value="">
                    <label class="form-check-label" for="flexCheckDefault">
                    </label>
                </div>
            </td>
            <td>
                <select class="form-control class-list" id="class-list-${i}" style="background-color: white; width: 88px;height: 35px;" >
               
                </select>
            </td>
            <td >
                <input type="hidden" id="id">
                <input type="text" class="form-control" id="name-${i}" required style="height: 35px;" required >


            </td>
            <td >
                <input type="date" class="form-control" id="dob-${i}" required style="height: 35px;">
            </td>
            <td >
                            <div > 
                           <div class="form-check " >
                              <input class="form-check-input gender-${i}" type="radio" name="gender-radio-${i}" id="gender1-${i}" value="male" checked>
                              <label class="form-check-label" for="exampleRadios1">
                               Male
                              </label>
                            </div>
                            <div class="form-check " >
                              <input class="form-check-input gender-${i}" type="radio" name="gender-radio-${i}" id="gender2-${i}" value="female">
                              <label class="form-check-label" for="exampleRadios2">
                                Female
                              </label>
                            </div>
                            </div>
            </td>
            <td >
                <input type="number" class="form-control" id="phone-${i}" required style="height: 35px;" required>


            </td>
            <td >
                <input type="text" class="form-control" id="note-${i}" required style="height: 35px;">
            </td>
        </tr>
        `);
        }

        $('.class-list').empty();
        for (let i = 0; i < classList.length; i++) {
            $('.class-list').append(`
               <option value="${classList[i].id}">${classList[i].nameClass}</option>
        `);
        }

        $("#flexCheckDefault").click(function () {
            $(".student-rows").prop('checked', $(this).prop('checked'));
        });

        $(".student-rows").change(function () {
            if (!$(this).prop("checked")) {
                $("#flexCheckDefault").prop("checked", false);
            }
        });
    },

    identifyGender() {
        let genderList = $('.gender-radio');
        for (let i = 0; i < 10; i++) {
            if (genderList[0].checked) {
                document.getElementById(`gender1-${i}`).checked = true;
                document.getElementById(`gender2-${i}`).checked = false;
            } else {
                document.getElementById(`gender1-${i}`).checked = false;
                document.getElementById(`gender2-${i}`).checked = true;
            }
        }
    },

    // batch input - all
    backInput() {

        let student = {
            name: $(`#name-back`).val(),
            gender: this.identifyGender(),
            phoneNumber: $(`#phone-back`).val(),
            dob: $(`#date-back`).val(),
            note: $(`#note-back`).val(),

        }
        student.studentClass = {
            id: Number($(`.class-list`).val())
        }

        for (let i = 0; i < 10; i++) {
            $(`#name-${i}`).val($(`#name-back`).val());

            $(`#phone-${i}`).val($(`#phone-back`).val());
            $(`#dob-${i}`).val($(`#date-back`).val());
            $(`#note-${i}`).val($(`#note-back`).val());
            $(`#class-list-${i}`).val(Number($(`.class-list`).val()))
        }
    },


    //save student - save all
    save() {
        $.ajax({
            url: `http://localhost:8080/api/student/showAll`,
            async: false,
            method: "GET",
            dataType: "json",
            success: function (data) {
                $('#showListStudent').empty();
                $.each(data, function (i, v) {

                    $('#showListStudent').append(
                        "<tr>" +
                        "<td>" + i + "</td>" +
                        "<td>" + v.student_id + "</td>" +
                        "<td>" + v.name + "</td>" +
                        "<td>" + v.dob + "</td>" +
                        "<td>" + v.gender + "</td>" +
                        "<td>" + v.phoneNumber + "</td>" +
                        "<td>" + v.note + "</td>" +
                        "</tr>"
                    );
                    showAllStudent.push(v.phoneNumber)
                    nameData.push(v.name)
                    dobData.push(v.dob)
                })
            }
        })


        let students = [];
        let phone = [];



        for (let i = 0; i < 10; i++) {

            let studentDTO = {};

            let student = {
                name: $(`#name-${i}`).val(),
                gender: document.querySelector(`input[name= gender-radio-${i}]:checked`).value,
                phoneNumber: $(`#phone-${i}`).val(),
                dob: $(`#dob-${i}`).val(),
                note: $(`#note-${i}`).val()
            }


            student.studentClass = {id: Number($(`#class-list-${i}`).val())};
            studentDTO.student = student;
            studentDTO.checked = document.querySelector(`#student-checker-${i}`).checked;

            if (student.name.length > 255) {
                a = i + 1
                alert('student_' + a + ': Name length is less than 255 characters')
                continue;
            }

            if ((student.name.trim() === "" || !student.phoneNumber.match(/^\d{10}$/))) {
                a = i + 1
                alert('student_' + a + ': Can\'t be blank Name and phone number must be in the correct format')
                continue;
            }
            //check phone
            for (let j = 0; j < phone.length; j++) {
                a = i + 1
                if (student.phoneNumber === phone[j]) {
                    alert('student_' + a + ': Duplicate phone number on screen')
                    break
                }
            }

            for (let j = 0; j < showAllStudent.length; j++) {
                a = i + 1
                if (student.phoneNumber === showAllStudent[j]) {
                    alert('student_' + a + ': Duplicate phone numbers in database')
                    break
                }
            }
//check name
            for (let j = 0; j < name.length; j++) {
                a = i + 1
                if (student.name === name[j] && student.name !== "") {
                    alert('student_' + a + ': Duplicate name student on screen')
                    break
                }
            }
            for (let j = 0; j < nameData.length; j++) {
                a = i + 1
                if (student.name === nameData[j] && student.name !== "") {
                    alert('student_' + a + ': Duplicate name student in database')
                    break
                }
            }
            //check dob
            for (let j = 0; j < dob.length; j++) {
                a = i + 1
                if (student.dob === dob[j] && student.dob !== "") {
                    alert('student_' + a + ': Duplicate date of birth on screen')
                    break
                }
            }
            for (let j = 0; j < dobData.length; j++) {
                a = i + 1
                if (student.dob === dobData[j] && student.dob !== "") {
                    alert('student_' + a + ': Duplicate date of birth in database')
                    break
                }
            }

            if (student.phoneNumber.match(/^\d{10}$/)) {
                phone.push(studentDTO.student.phoneNumber)
            }
            name.push(student.name)
            dob.push(student.dob)


            if ((studentDTO.student.name !== "" && student.phoneNumber.match(/^\d{10}$/) && student.name.length < 255 && showAllStudent.indexOf(student.phoneNumber) === -1 && dobData.indexOf(student.dob) === -1 && nameData.indexOf(student.name) === -1)) {
                students.push(studentDTO);
            }


        }
        console.log(showAllStudent)
        console.log(phone)
        console.log(students)
        $.ajax({
            url: `http://localhost:8080/api/student`,
            async: false,
            method: "POST",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(students),
            success: function () {
                if (students.length === 0) {
                    alert('No students are added')
                } else {
                    alert('Create successfully!!!')
                }

                for (let i = 0; i < 10; i++) {
                    $(`#name-${i}`).val("");
                    $(`#gender-${i}`).val("male");
                    $(`#phone-${i}`).val("");
                    $(`#dob-${i}`).val("");
                    $(`#note-${i}`).val("");
                    $(`#class-list-${i}`).val(1);
                    document.querySelector(`#student-checker-${i}`).checked = false;
                }
                phone.length = 0
                name.length = 0
                dob.length = 0

            },
            error: function () {
                alert('No students are added')
            }

        });


    },
    confirm() {
        if (confirm("Are you sure you want to delete?") == true) {
            student.clearForm()
        }
    },


    // clear data
    clearForm() {
        for (let i = 0; i < 10; i++) {
            if (document.querySelector(`#student-checker-${i}`).checked) {
                $(`#name-${i}`).val("");
                document.getElementById(`gender1-${i}`).checked = true;
                document.getElementById(`gender2-${i}`).checked = false;
                $(`#phone-${i}`).val("");
                $(`#dob-${i}`).val("");
                $(`#note-${i}`).val("");
                $(`#class-list-${i}`).val(1);
                document.querySelector(`#student-checker-${i}`).checked = false;
                count--
            }
            if (count < 0) {
                count += 10
            }
        }
        console.log(count)
        if (count === 10) {
            alert('If you want to delete the data, you need to click the checkbox')
        } else {
            alert('delete success!!!')
        }
        count = 10
    }

};
//exit
let exit = {
    initPage() {
        $('#index-exit').empty();
        $('#index-exit').append(`
       <div>
       <h1 style="margin-left: 40%; margin-top: 20%">Goodbye....!</h1>
</div>
    `)

    }
}


//check box all
$('#flexCheckDefault').change(function () {
    $('.student-rows').prop('checked', this.checked);
});

$('.student-rows').change(function () {
    if ($('.student-rows:checked').length === $('.student-rows').length) {
        $('#flexCheckDefault').prop('checked', true);
    } else {
        $('#flexCheckDefault').prop('checked', false);
    }
});

//run
$(document).ready(function () {
    classStudent.init();
    display.initPage();
    display.displayClass();

});