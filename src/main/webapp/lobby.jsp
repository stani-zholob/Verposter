<!-- Author: Stanislav -->
<!DOCTYPE html>
<html lang="de">
<head>
    <meta charset="UTF-8">
    <title>Verposter - Lobbies</title>
</head>
<body>

<h1>VERPOSTER</h1>

<h2>Hello</h2>
<h2>Room List</h2>

<form action = "/Verposter/createroom" method = "get">
    <p><button type="submit">+ New Lobby</button></p></form>

<table border="1" cellpadding="5">
    <thead>
        <tr>
            <th>Room</th>
            <th>Name</th>
            <th></th>

        </tr>
    </thead>

    <tbody id="rooms-table">

    </tbody>
</table>
<form action="/Verposter/logout" method="get">
<br> <button>Log Out</button>
</form>
<script>
    async function getRooms(){
        const resp = await fetch('/Verposter/api/rooms');
        const rooms = await resp.json();
        console.log(rooms);


        const table = document.getElementById("rooms-table");
        for(const room of rooms){
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${room.id}</td>
                <td>${room.name}</td>
                <td> <form action="/gamestart.html" method="get"> <button type="button" name = "join"> Join </button> </form> </td> `;
            table.append(row);
        }
    }
    getRooms();

    async function getUsername(){

    }
</script>

</body>
</html>
