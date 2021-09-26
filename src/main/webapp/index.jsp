<!DOCTYPE html>
<html>
<head>
    <title>UDP Test</title>
    <link href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700" rel="stylesheet">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.4.1/css/all.css"
          integrity="sha384-5sAR7xN1Nv6T6+dT2mhtzEpVJvfS3NScPQTrOxhwjIuvcA67KV2R5Jz6kr4abQsz" crossorigin="anonymous">
    <style>
        html, body {
            height: 100%;
        }

        body, h1, h3, input {
            padding: 0;
            margin: 0;
            outline: none;
            font-family: Roboto, Arial, sans-serif;
            font-size: 16px;
            color: #666;
        }

        h1, h3 {
            padding: 12px 0;
            font-weight: 400;
        }

        h1 {
            font-size: 28px;
        }

        .main-block, .info {
            display: flex;
            flex-direction: column;
        }

        form {
            width: 50%;
            padding: 20px;
            margin: 0 auto;
            border-radius: 5px;
            box-shadow: 1px 2px 5px rgba(0, 0, 0, .31);
            background: #ebebeb;
        }

        .info-item {
            width: 70%;
        }

        .info-item-btn {
            width: 30%;
        }

        input {
            width: calc(100% - 57px);
            height: 36px;
            padding-left: 10px;
            margin: 0 0 12px -5px;
            border-radius: 0 5px 5px 0;
            border: solid 1px #cbc9c9;
            box-shadow: 1px 2px 5px rgba(0, 0, 0, .09);
            background: #fff;
        }

        .icon {
            padding: 9px 15px;
            margin-top: -1px;
            border-radius: 5px 0 0 5px;
            border: solid 0px #cbc9c9;
            background: #666;
            color: #fff;
        }

        button {
            width: 100%;
            padding: 10px;
            border-radius: 5px;
            border: none;
            background: #8ebf42;
            font-size: 14px;
            font-weight: 600;
            color: #fff;
        }

        button:hover {
            background: #82b534;
            cursor: pointer;
        }

        @media (min-width: 568px) {
            .info {
                flex-flow: row wrap;
                justify-content: center;
            }

            .info-item {
                width: 48%;
            }
        }

        table {
            border-collapse: collapse;
            width: 50%;
            margin: 0 auto;
        }

        table tr td, table tr th {
            border: 1px solid;
        }
    </style>
</head>
<body>
<div class="main-block">
    <h1 style="width: 50%; margin: 0 auto;">UDP Test Form</h1>
    <form action="/">
        <div class="info">
            <div class="info-item">
                <label class="icon" for="url"><i class="fas fa-globe"></i></label>
                <input type="url" name="name" id="url" placeholder="enter domain name" required/>
            </div>
            <div class="info-item-btn">
                <button type="button" onclick="submitForm()">Submit</button>
            </div>
        </div>
    </form>
    <div id="table-container" class="main-block form">

    </div>
</div>
<script>

    function submitForm() {
        const str = document.getElementById("url").value;
        if (validURL(str)) {
            ajaxPost(str);
        } else {
            alert("The url " + str + " is not valid");
        }
    }

    function validURL(str) {
        const pattern = new RegExp('^(https?:\\/\\/)?' + // protocol
            '((([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.)+[a-z]{2,}|' + // domain name
            '((\\d{1,3}\\.){3}\\d{1,3}))' + // OR ip (v4) address
            '(\\:\\d+)?(\\/[-a-z\\d%_.~+]*)*' + // port and path
            '(\\?[;&a-z\\d%_.~+=-]*)?' + // query string
            '(\\#[-a-z\\d_]*)?$', 'i'); // fragment locator
        return !!pattern.test(str);
    }

    function ajaxPost(str) {
        const contextPath = '<%=request.getContextPath()%>';
        const xmlHttp = new XMLHttpRequest();
        xmlHttp.onreadystatechange = function () {
            if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
                const jsonResponse = JSON.parse(xmlHttp.responseText);
                if (jsonResponse['message']) {
                    alert(jsonResponse['message']);
                    return;
                }
                const data = jsonResponse['data'];
                document.getElementById("table-container").innerHTML = '';
                createTable(data);
            }
        }
        xmlHttp.open("post", contextPath + "/udp?url=" + str+"&random="+Math.random());
        xmlHttp.send();
    }

    function createTable(tableData) {
        const table = document.createElement('table');
        const tableHead = document.createElement('thead');
        const row = document.createElement('tr');
        const cell1 = document.createElement('th');
        row.appendChild(cell1);
        cell1.appendChild(document.createTextNode('IP Address'));
        const cell2 = document.createElement('th');
        cell2.appendChild(document.createTextNode('Port'));
        row.appendChild(cell2);
        const cell3 = document.createElement('th');
        cell3.appendChild(document.createTextNode('Domain'));
        row.appendChild(cell3);
        const cell4 = document.createElement('th');
        cell4.appendChild(document.createTextNode('Delay Time in (ms)'));
        row.appendChild(cell4);
        tableHead.appendChild(row);
        table.appendChild(tableHead)
        const tableBody = document.createElement('tbody');
        tableData.forEach(function (rowData) {
            const row = document.createElement('tr');
            const cell1 = document.createElement('td');
            cell1.appendChild(document.createTextNode(rowData['ip']));
            row.appendChild(cell1);
            const cell2 = document.createElement('td');
            cell2.appendChild(document.createTextNode(rowData['port']));
            row.appendChild(cell2);
            const cell3 = document.createElement('td');
            cell3.appendChild(document.createTextNode(rowData['host']));
            row.appendChild(cell3);
            const cell4 = document.createElement('td');
            cell4.appendChild(document.createTextNode(rowData['delay']));
            row.appendChild(cell4);
            tableBody.appendChild(row);
        });
        table.appendChild(tableBody);
        document.getElementById("table-container").appendChild(table);
    }
</script>
</body>
</html>
