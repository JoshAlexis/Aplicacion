<?PHP
  include("dbConexion.php");
  $dbConexion=dbConexion();
  $json=array();

  if(isset($_GET["nombre"]) && isset($_GET["paterno"]) && isset($_GET["materno"]) && isset($_GET["usuario"]) && isset($_GET["contrasena"]) && isset($_GET["perfil"])) {
    $nombre=$_GET['nombre'];
    $paterno=$_GET['paterno'];
    $materno=$_GET['materno'];
    $usuario=$_GET['usuario'];
    $contrasena=$_GET['contrasena'];
    $perfil=$_GET['perfil'];

    $consulta="INSERT INTO usuarios VALUES(NULL,'$nombre','$paterno','$materno','$usuario','$contrasena',$perfil,CURDATE(),1)";

    $resultado=mysqli_query($dbConexion,$consulta);

    if($registro=mysqli_fetch_array($resultado)){
      $resu["idUsuarios"]="Agregado";
      $json['usuario'][]=$registro;
    }
  }
  mysqli_close($dbConexion);
  echo json_encode($json);
?>