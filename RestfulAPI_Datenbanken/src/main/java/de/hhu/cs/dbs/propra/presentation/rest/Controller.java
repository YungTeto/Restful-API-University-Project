package de.hhu.cs.dbs.propra.presentation.rest;

import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.sql.DataSource;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Path("/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public class Controller {
    @Inject
    private DataSource dataSource;

    @Context
    private SecurityContext securityContext;

    @Context
    private UriInfo uriInfo;

//    @GET // GET http://localhost:8080
//    public String halloWelt() {
//        return "Hallo Welt!";
//    }

//    @Path("foo")
//    @RolesAllowed({"USER", "EMPLOYEE", "ADMIN"})
//    @GET
//    // GET http://localhost:8080/foo => OK, wenn Benutzer die Rolle "USER", "EMPLOYEE" oder "ADMIN" hat. Siehe SQLiteUserRepository.
//    public String halloFoo() {
//        return "Hallo " + securityContext.getUserPrincipal() + "!";
//    }
//
//    @Path("foo2/{bar}")
//    @GET // GET http://localhost:8080/foo/xyz
//    public String halloFoo2(@PathParam("bar") String bar) {
//        if (!bar.equals("foo")) throw new NotFoundException("Resource '" + bar + "' not found");
//        return "Hallo " + bar + "!";
//    }
//
//    @Path("foo3")
//    @GET // GET http://localhost:8080/foo3?bar=xyz
//    public String halloFoo3(@QueryParam("bar") String bar) {
//        return "Hallo " + bar + "!";
//    }
//
//    @Path("bar")
//    @GET // GET http://localhost:8080/bar => Bad Request; http://localhost/bar?foo=xyz => OK
//    public Response halloBar(@QueryParam("foo") String foo) {
//        if (foo == null) throw new BadRequestException();
//        return Response.status(Response.Status.OK).entity("Hallo Bar!").build();
//    }
//
//    @Path("bar2")
//    @GET // GET http://localhost:8080/bar2
//    public List<Map<String, Object>> halloBar2(@QueryParam("name") @DefaultValue("Max Mustermann") List<String> names) throws SQLException {
//        Connection connection = dataSource.getConnection();
//        PreparedStatement preparedStatement = connection.prepareStatement("SELECT ?;");
//        preparedStatement.closeOnCompletion();
//        int random = ThreadLocalRandom.current().nextInt(0, names.size());
//        preparedStatement.setObject(1, names.get(random));
//        ResultSet resultSet = preparedStatement.executeQuery();
//        List<Map<String, Object>> entities = new ArrayList<>();
//        Map<String, Object> entity;
//        while (resultSet.next()) {
//            entity = new HashMap<>();
//            entity.put("name", resultSet.getObject(1));
//            entities.add(entity);
//        }
//        resultSet.close();
//        connection.close();
//        return entities;
//    }
//
//
//
//    @Path("foo")
//    @POST // POST http://localhost:8080/foo
//    public Response einUpload(@FormDataParam("name") String name, @FormDataParam("file") InputStream file) {
//        if (name == null) return Response.status(Response.Status.BAD_REQUEST).build();
//        if (file == null) return Response.status(Response.Status.BAD_REQUEST).build();
//        byte[] bytes;
//        try {
//            bytes = IOUtils.toByteArray(file);
//        } catch (IOException e) {
//            return Response.status(Response.Status.BAD_REQUEST).build();
//        }
//        return Response.created(uriInfo.getAbsolutePathBuilder().path("234235").build()).build();
//    }

//    @Path("foo")
//    @POST // POST http://localhost:8080/foo
//        //curl -X POST http://localhost:8080/foo -F "name=daniel" -v
//    public Response einUpload(@FormDataParam("name") String name) {
//        if (name == null) return Response.status(Response.Status.BAD_REQUEST).build();
//        return Response.created(uriInfo.getAbsolutePathBuilder().path("234235").build()).build();
//    }



    @Path("nutzer")
    @GET // GET http://localhost:8080/nutzer
    public List<Map<String, Object>> getNutzer(@QueryParam("email") String email) throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT rowid, * FROM Nutzer;");
        if(email != null){
            preparedStatement = connection.prepareStatement("SELECT rowid, * FROM Nutzer WHERE Email LIKE ?;");
            preparedStatement.setObject(1, "%" + email+"%");
        }
        preparedStatement.closeOnCompletion();

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, Object>> entities = new ArrayList<>();
        Map<String, Object> entity;
        while (resultSet.next()) {
            entity = new LinkedHashMap<>();
            entity.put("nutzerid", resultSet.getObject(1));
            entity.put("email", resultSet.getObject(2));
            entity.put("passwort", resultSet.getObject(3));
            entities.add(entity);
        }
        resultSet.close();
        connection.close();
        return entities;
    }

    @Path("nutzer")
    @POST // POST http://localhost:8080/nutzer
    public Response postNutzer(@FormDataParam("email") String email, @FormDataParam("passwort") String passwort) throws SQLException {
        Connection connection = dataSource.getConnection();

        try {
            if (email == null){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (passwort == null){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Nutzer VALUES (?,?);");
            preparedStatement.closeOnCompletion();
            preparedStatement.setObject(1,email);
            preparedStatement.setObject(2,passwort);
            preparedStatement.executeUpdate();

            return Response.created(uriInfo.getAbsolutePathBuilder().path(email).build()).build();

        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        finally {
            connection.close();
        }
    }

    @Path("kunden")
    @GET // GET http://localhost:8080/kunden
    public List<Map<String, Object>> getKunden(@QueryParam("email") String email, @QueryParam("telefonnummer") String telefonnummer) throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT Nutzer.rowid, Kunde.rowid, * FROM Nutzer, Kunde WHERE Kunde.Email = Nutzer.Email;");

        if(email != null && telefonnummer != null){
            preparedStatement = connection.prepareStatement("SELECT n.ROWID AS nutzerowid , k.ROWID AS kunderowid ,* FROM Nutzer n, Kunde k WHERE k.Email = n.Email AND k.Email LIKE ? AND k.Telefonnummer LIKE ?;");
            preparedStatement.setObject(1, "%" + email+"%");
            preparedStatement.setObject(2, "%" + telefonnummer+"%");
        }
        if(email != null && telefonnummer == null){
            preparedStatement = connection.prepareStatement("SELECT n.ROWID AS nutzerowid , k.ROWID AS kunderowid ,* FROM Nutzer n, Kunde k WHERE k.Email = n.Email AND k.Email LIKE ?;");
            preparedStatement.setObject(1, "%" + email+"%");
        }
        //preparedStatement.closeOnCompletion();
        if(telefonnummer != null && email == null){
            preparedStatement = connection.prepareStatement("SELECT n.ROWID AS nutzerowid , k.ROWID AS kunderowid ,* FROM Nutzer n, Kunde k WHERE k.Email = n.Email AND k.Telefonnummer LIKE ?;");
            preparedStatement.setObject(1, "%" + telefonnummer+"%");
        }
        preparedStatement.closeOnCompletion();

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, Object>> entities = new ArrayList<>();
        Map<String, Object> entity;
        while (resultSet.next()) {
            entity = new LinkedHashMap<>();
            entity.put("nutzerid", resultSet.getObject(1));
            entity.put("kundenid", resultSet.getObject(2));
            entity.put("email", resultSet.getObject(3));
            entity.put("passwort", resultSet.getObject(4));
            entity.put("telefon", resultSet.getObject(5));
            entities.add(entity);
        }
        resultSet.close();
        connection.close();
        return entities;
    }

    @Path("kunden")
    @POST // POST http://localhost:8080/kunden
    public Response postKunden(@FormDataParam("email") String email, @FormDataParam("passwort") String passwort, @FormDataParam("telefon") String telefonnummer) throws SQLException {
        Connection connection = dataSource.getConnection();

        try {
            if (email == null){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (passwort == null){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (telefonnummer == null){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }


            //Erstelle Nutzer
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Nutzer VALUES (?,?);");
            preparedStatement.closeOnCompletion();
            preparedStatement.setObject(1,email);
            preparedStatement.setObject(2,passwort);
            preparedStatement.executeUpdate();

            //Erstelle Kunde
            PreparedStatement preparedStatement1 = connection.prepareStatement("INSERT INTO Kunde VALUES (?,?);");
            preparedStatement1.closeOnCompletion();
            preparedStatement1.setObject(1,telefonnummer);
            preparedStatement1.setObject(2,email);
            preparedStatement1.executeUpdate();

            return Response.created(uriInfo.getAbsolutePathBuilder().path(email).build()).build();

        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        finally {
            connection.close();
        }
    }

    @Path("projektleiter")
    @GET // GET http://localhost:8080/projektleiter
    public List<Map<String, Object>> getProjektleiter(@QueryParam("email") String email, @QueryParam("gehalt") String gehalt) throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT Nutzer.rowid, Projektleiter.rowid, * FROM Nutzer, Projektleiter WHERE Projektleiter.Email = Nutzer.Email;");

        if(email!= null && gehalt != null){
            preparedStatement = connection.prepareStatement("SELECT n.ROWID AS nutzerowid , p.ROWID AS projektleiterrowid ,* FROM Nutzer n, Projektleiter p WHERE p.Email = n.Email AND p.Email LIKE ? and p.Gehalt > ?;");
            preparedStatement.setObject(1, "%" + email+"%");
            preparedStatement.setObject(2,  gehalt );
        }
        if(email != null && gehalt == null){
            preparedStatement = connection.prepareStatement("SELECT n.ROWID AS nutzerowid , p.ROWID AS projektleiterrowid ,* FROM Nutzer n, Projektleiter p WHERE p.Email = n.Email AND p.Email LIKE ?;");
            preparedStatement.setObject(1, "%" + email+"%");
        }
        //preparedStatement.closeOnCompletion();
        if(gehalt != null && email == null){
            preparedStatement = connection.prepareStatement("SELECT n.ROWID AS nutzerowid , p.ROWID AS projektleiterrowid ,* FROM Nutzer n, Projektleiter p WHERE p.Email = n.Email AND p.Gehalt > ?;");
            preparedStatement.setObject(1, gehalt);
        }
        preparedStatement.closeOnCompletion();

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, Object>> entities = new ArrayList<>();
        Map<String, Object> entity;
        while (resultSet.next()) {
            entity = new LinkedHashMap<>();
            entity.put("nutzerid", resultSet.getObject(1));
            entity.put("projektleiterid", resultSet.getObject(2));
            entity.put("email", resultSet.getObject(3));
            entity.put("passwort", resultSet.getObject(4));
            entity.put("gehalt", resultSet.getObject(6));
            entities.add(entity);
        }
        resultSet.close();
        connection.close();
        return entities;
    }

    @Path("projektleiter")
    @POST // POST http://localhost:8080/nutzer
    public Response postProjektleiter(@FormDataParam("email") String email, @FormDataParam("passwort") String passwort, @FormDataParam("gehalt") Integer gehalt) throws SQLException {
        Connection connection = dataSource.getConnection();

        try {
            if (email == null){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (passwort == null){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (gehalt == null){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }


            //Erstelle Nutzer
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Nutzer VALUES (?,?);");
            preparedStatement.closeOnCompletion();
            preparedStatement.setObject(1,email);
            preparedStatement.setObject(2,passwort);
            preparedStatement.executeUpdate();

            //Erstelle Projektleiter
            PreparedStatement preparedStatement1 = connection.prepareStatement("INSERT INTO Projektleiter VALUES (?,?);");
            preparedStatement1.closeOnCompletion();
            preparedStatement1.setObject(1,email);
            preparedStatement1.setObject(2,gehalt);
            preparedStatement1.executeUpdate();

            return Response.created(uriInfo.getAbsolutePathBuilder().path(email).build()).build();

        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        finally {
            connection.close();
        }
    }

    @Path("spezialisten")
    @GET // GET http://localhost:8080/spezialisten
    public List<Map<String, Object>> getSpezialisten(@QueryParam("email") String email, @QueryParam("verfuegbar") String verfuegbar) throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT Nutzer.rowid, Spezialist.rowid, * FROM Nutzer, Spezialist WHERE Spezialist.Email = Nutzer.Email;");

        if(email!= null && verfuegbar != null){
            preparedStatement = connection.prepareStatement("SELECT n.ROWID AS nutzerowid , s.ROWID AS spezialistrowid ,* FROM Nutzer n, Spezialist s WHERE s.Email = n.Email AND s.Email LIKE ? AND s.Verfuegbarkeitsstatus = ?;");
            preparedStatement.setObject(1, "%" + email+"%");
            preparedStatement.setObject(2,   verfuegbar );
        }
        if(email != null && verfuegbar == null){
            preparedStatement = connection.prepareStatement("SELECT n.ROWID AS nutzerowid , s.ROWID AS spezialistrowid ,* FROM Nutzer n, Spezialist s WHERE s.Email = n.Email AND s.Email LIKE ?;");
            preparedStatement.setObject(1, "%" + email+"%");
        }
        //preparedStatement.closeOnCompletion();
        if(verfuegbar != null && email == null){
            preparedStatement = connection.prepareStatement("SELECT n.ROWID AS nutzerowid , s.ROWID AS spezialistrowid ,* FROM Nutzer n, Spezialist s WHERE s.Email = n.Email AND s.Verfuegbarkeitsstatus = ?;");
            preparedStatement.setObject(1,  verfuegbar );
        }
        preparedStatement.closeOnCompletion();

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, Object>> entities = new ArrayList<>();
        Map<String, Object> entity;
        while (resultSet.next()) {
            entity = new LinkedHashMap<>();
            entity.put("nutzerid", resultSet.getObject(1));
            entity.put("spezialistid", resultSet.getObject(2));
            entity.put("email", resultSet.getObject(3));
            entity.put("passwort", resultSet.getObject(4));
            entity.put("verfuegbarkeitsstatus", resultSet.getObject(6));
            entities.add(entity);
        }
        resultSet.close();
        connection.close();
        return entities;
    }

    @Path("spezialisten")
    @POST // POST http://localhost:8080/spezialisten
    public Response postSpezialisten(@FormDataParam("email") String email, @FormDataParam("passwort") String passwort, @FormDataParam("verfuegbarkeitsstatus") String verfuegbarkeitsstatus) throws SQLException {
        Connection connection = dataSource.getConnection();

        try {
            if (email == null){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (passwort == null){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (verfuegbarkeitsstatus == null){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }


            //Erstelle Nutzer
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Nutzer VALUES (?,?);");
            preparedStatement.closeOnCompletion();
            preparedStatement.setObject(1,email);
            preparedStatement.setObject(2,passwort);
            preparedStatement.executeUpdate();

            //Erstelle spezialisten
            PreparedStatement preparedStatement1 = connection.prepareStatement("INSERT INTO Spezialist VALUES (?,?);");
            preparedStatement1.closeOnCompletion();
            preparedStatement1.setObject(1,email);
            preparedStatement1.setObject(2,verfuegbarkeitsstatus);
            preparedStatement1.executeUpdate();

            return Response.created(uriInfo.getAbsolutePathBuilder().path(email).build()).build();

        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        finally {
            connection.close();
        }
    }

    @Path("projekte")
    @GET // GET http://localhost:8080/projekte
    public List<Map<String, Object>> getProjekt() throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT ProjektID,Projektname,Projektdeadline FROM Projekt;");

        preparedStatement.closeOnCompletion();

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, Object>> entities = new ArrayList<>();
        Map<String, Object> entity;
        while (resultSet.next()) {
            entity = new LinkedHashMap<>();
            entity.put("projektid", resultSet.getObject(1));
            entity.put("name", resultSet.getObject(2));
            entity.put("deadline", resultSet.getObject(3));
            entities.add(entity);
        }
        resultSet.close();
        connection.close();
        return entities;
    }

    @Path("projekte/{projektid}/bewertungen")
    @GET
    public List<Map<String, Object>> getBewertungen(@PathParam("projektid") int projektid) throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT BewertungID, Bepunktung, Text FROM Bewertung WHERE Bewertung.ProjektID= ?;");
        preparedStatement.closeOnCompletion();
        preparedStatement.setObject(1,projektid);

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, Object>> entities = new ArrayList<>();
        Map<String, Object> entity;
        while (resultSet.next()) {
            entity = new LinkedHashMap<>();
            entity.put("bewertungid", resultSet.getObject(1));
            entity.put("punktzahl", resultSet.getObject(2));
            entity.put("text", resultSet.getObject(3));
            entities.add(entity);
        }
        resultSet.close();
        connection.close();
        return entities;
    }

    @Path("projekte/{projektid}/aufgaben")
    @GET
    public List<Map<String, Object>> getAufgaben(@PathParam("projektid") Integer projektid) throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT a.ROWID, Aufgabendeadline, Beschreibung, Status, Priorisierung FROM Aufgaben a WHERE ProjektID = ?;");
        preparedStatement.closeOnCompletion();
        preparedStatement.setObject(1, projektid);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, Object>> entities = new ArrayList<>();
        Map<String, Object> entity;
        while (resultSet.next()) {
            entity = new LinkedHashMap<>();
            entity.put("aufgabeid", resultSet.getObject(1));
            entity.put("deadline", resultSet.getObject(2));
            entity.put("beschreibung", resultSet.getObject(3));
            entity.put("status", resultSet.getObject(4));
            entity.put("prioritaet", resultSet.getObject(5));
            entities.add(entity);
        }
        resultSet.close();
        connection.close();
        return entities;
    }

    @Path("projekte/{projektid}/spezialisten")
    @GET
    public List<Map<String, Object>> getSpezialisten(@PathParam("projektid") int projektid) throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT s.ROWID, s.Verfuegbarkeitsstatus, s.Email, n.Passwort FROM Spezialist s, Nutzer n, arbeitet a WHERE s.Email = n.Email AND s.Email = a.email AND a.ProjektID = ?;");
        preparedStatement.closeOnCompletion();
        preparedStatement.setObject(1,projektid);

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, Object>> entities = new ArrayList<>();
        Map<String, Object> entity;
        while (resultSet.next()) {
            entity = new LinkedHashMap<>();
            entity.put("spezialistid", resultSet.getObject(1));
            entity.put("verfuegbarkeitsstatus", resultSet.getObject(2));
            entity.put("email", resultSet.getObject(3));
            entity.put("passwort", resultSet.getObject(4));
            entities.add(entity);
        }
        resultSet.close();
        connection.close();
        return entities;
    }


    @Path("entwickler")
    @GET
    public List<Map<String, Object>> getEntwickler(@QueryParam("kuerzel") String kuerzel) throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT n.rowid as nutzerid, s.rowid as spezialistid, e.ROWID as entwicklerid, * FROM Nutzer n, Spezialist s, Entwickler e WHERE s.Email = n.Email AND e.Email = s.Email;");
        if(kuerzel != null){
            preparedStatement = connection.prepareStatement("SELECT n.rowid as nutzerid, s.rowid as spezialistid, e.ROWID as entwicklerid, * FROM Nutzer n, Spezialist s, Entwickler e WHERE s.Email = n.Email AND e.Email = s.Email AND e.kuerzel LIKE ?;");
            preparedStatement.setObject(1, "%" + kuerzel+"%");
        }
        preparedStatement.closeOnCompletion();

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, Object>> entities = new ArrayList<>();
        Map<String, Object> entity;
        while (resultSet.next()) {
            entity = new LinkedHashMap<>();
            entity.put("nutzerid", resultSet.getObject(1));
            entity.put("spezialistid", resultSet.getObject(2));
            entity.put("entwicklerid", resultSet.getObject(3));
            entity.put("email", resultSet.getObject(4));
            entity.put("passwort", resultSet.getObject(5));
            entity.put("verfuegbarkeitsstatus", resultSet.getObject(7));
            entity.put("kuerzel", resultSet.getObject(8));

            entities.add(entity);
        }
        resultSet.close();
        connection.close();
        return entities;
    }

    @Path("entwickler")
    @POST
    public Response postEntwickler(@FormDataParam("email") String email, @FormDataParam("passwort") String passwort, @FormDataParam("verfuegbarkeitsstatus") String verfuegbarkeitsstatus, @FormDataParam("kuerzel") String kuerzel, @FormDataParam("benennung") String benennung )throws SQLException {
        Connection connection = dataSource.getConnection();

        try {
            if (email == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (passwort == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (verfuegbarkeitsstatus == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (kuerzel == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (benennung == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            //Erstelle Nutzer
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Nutzer VALUES (?,?);");
            preparedStatement.closeOnCompletion();
            preparedStatement.setObject(1, email);
            preparedStatement.setObject(2, passwort);
            preparedStatement.executeUpdate();

            //Erstelle Spezialisten
            PreparedStatement preparedStatement1 = connection.prepareStatement("INSERT INTO Spezialist VALUES (?,?);");
            preparedStatement1.closeOnCompletion();
            preparedStatement1.setObject(1, email);
            preparedStatement1.setObject(2, verfuegbarkeitsstatus);
            preparedStatement1.executeUpdate();

            //Erstelle Entwickler
            PreparedStatement preparedStatement2 = connection.prepareStatement("INSERT INTO Entwickler VALUES (?,?);");
            preparedStatement2.closeOnCompletion();
            preparedStatement2.setObject(1, kuerzel);
            preparedStatement2.setObject(2, email);
            preparedStatement2.executeUpdate();

            //Erstelle Programmiersprache
            boolean duplicate = containsProgrammiersprache(benennung);
            PreparedStatement preparedStatement3;
            if(!duplicate){
                preparedStatement3 = connection.prepareStatement("INSERT INTO Programmiersprache VALUES (?,?);");
                int nextid = getNextIDProgrammiersprache();
                preparedStatement3.setObject(1, String.valueOf(nextid));
//                pid=nextid;
            }
            else {
                preparedStatement3 = connection.prepareStatement("UPDATE Programmiersprache SET Name = ? WHERE ProgrammierspracheID = ?;");
                int id = getProgrammierspracheID(benennung);
                preparedStatement3.setObject(1, benennung);
                preparedStatement3.setObject(2, String.valueOf(id));
            }
            preparedStatement3.closeOnCompletion();
            preparedStatement3.setObject(2, benennung);
            preparedStatement3.executeUpdate();

            //Erstelle beherrscht
            PreparedStatement preparedStatement4 = connection.prepareStatement("INSERT INTO beherrscht VALUES (?,?,1);");
            preparedStatement4.closeOnCompletion();
            int id = getProgrammierspracheID(benennung);
            preparedStatement4.setObject(1, id);
            preparedStatement4.setObject(2, kuerzel);
            preparedStatement4.executeUpdate();

            return Response.created(uriInfo.getAbsolutePathBuilder().path(email).build()).build();

        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        finally {
            connection.close();
        }
    }

    @Path("programmierer")
    @GET
    public Response getProgramm() throws SQLException {
        URI uri = uriInfo.getBaseUriBuilder().path("entwickler").build();
        return Response.status(Response.Status.MOVED_PERMANENTLY).location(uri).build();
    }


    @Path("projekte")
    @RolesAllowed({"KUNDE"})
    @POST // POST http://localhost:8080/nutzer
    public Response postProjekte(@FormDataParam("name") String name, @FormDataParam("deadline") String deadline)throws SQLException {
        Connection connection = dataSource.getConnection();

        try {
            if (name == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (deadline == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }


            //Finde Telefonnummer
            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT  Telefonnummer FROM Kunde WHERE Email= ?");
            preparedStatement1.closeOnCompletion();
            String kundenemail= securityContext.getUserPrincipal().getName();
            preparedStatement1.setString(1, kundenemail);
            ResultSet resultSet = preparedStatement1.executeQuery();
            Object telefonnummer = resultSet.getObject(1);

            //Erstelle Projekt
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Projekt VALUES (?,?,?,?,'projektdude@gmail.com');");
            preparedStatement.closeOnCompletion();
            int nextid = getNextProjektID();
            preparedStatement.setObject(1, String.valueOf(nextid));
            preparedStatement.setObject(2, name);
            preparedStatement.setObject(3, deadline);
            preparedStatement.setObject(4, telefonnummer);
            preparedStatement.executeUpdate();

            return Response.created(uriInfo.getAbsolutePathBuilder().path(name).build()).build();

        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        finally {
            connection.close();
        }
    }



    @Path("projekte/{projektid}/bewertungen")
    @RolesAllowed({"KUNDE"})
    @POST
    public Response postBewertungen(@PathParam("projektid") Integer projektid, @FormDataParam("punktzahl") Integer punktzahl, @FormDataParam("text") String text)throws SQLException {
        Connection connection = dataSource.getConnection();

        try {
            if (projektid == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (punktzahl == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            //Finde Telefonnummer
            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT  Telefonnummer FROM Kunde WHERE Email= ?");
            preparedStatement1.closeOnCompletion();
            String kundenemail= securityContext.getUserPrincipal().getName();
            preparedStatement1.setString(1, kundenemail);
            ResultSet resultSet = preparedStatement1.executeQuery();
            Object telefonnummer = resultSet.getObject(1);

            //Not Found Abfrage
            PreparedStatement preparedStatement3 = connection.prepareStatement("SELECT * FROM Projekt WHERE ProjektID = ?;");
            preparedStatement3.closeOnCompletion();
            preparedStatement3.setObject(1, projektid);
            ResultSet resultSet3 = preparedStatement3.executeQuery();

            if(!resultSet3.next()){
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            //Erstelle Bewertung
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Bewertung VALUES (?,?,?,?,?);");
            preparedStatement.closeOnCompletion();
            int nextid = getNextBewertungID();
            preparedStatement.setObject(1, String.valueOf(nextid));
            preparedStatement.setObject(2, punktzahl);
            preparedStatement.setObject(3, telefonnummer);
            preparedStatement.setObject(4, projektid);
            preparedStatement.setObject(5, text);
            preparedStatement.executeUpdate();

            return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(nextid)).build()).build();
            //return Response.status(Response.Status.NO_CONTENT).build();

        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        finally {
            connection.close();
        }
    }

    @Path("bewertungen/{bewertungid}")
    @RolesAllowed({"KUNDE"})
    @PATCH
    public Response patchBewertungen(@PathParam("bewertungid") Integer bewertungid, @FormDataParam("punktzahl") Integer punktzahl, @FormDataParam("text") String text)throws SQLException {
        Connection connection = dataSource.getConnection();

        try {
            if (bewertungid == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (punktzahl == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }



            //Finde Telefonnummer
            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT  Telefonnummer FROM Kunde WHERE Email= ?");
            preparedStatement1.closeOnCompletion();
            String kundenemail= securityContext.getUserPrincipal().getName();
            preparedStatement1.setString(1, kundenemail);
            ResultSet resultSet = preparedStatement1.executeQuery();
            Object telefonnummer = resultSet.getObject(1);

            //Not Found Abfrage
            PreparedStatement preparedStatement3 = connection.prepareStatement("SELECT * FROM Bewertung WHERE BewertungID= ?;");
            preparedStatement3.closeOnCompletion();
            preparedStatement3.setObject(1, bewertungid);
            ResultSet resultSet3 = preparedStatement3.executeQuery();

            if(!resultSet3.next()){
                return Response.status(Response.Status.NOT_FOUND).build();
            }


            //Forbidden Abfrage
            PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT * FROM Bewertung WHERE BewertungID= ? AND Telefonnummer = ?;");
            preparedStatement2.closeOnCompletion();
            preparedStatement2.setObject(1, String.valueOf(bewertungid));
            preparedStatement2.setObject(2, telefonnummer);
            ResultSet resultSet2 = preparedStatement2.executeQuery();

            if(!resultSet2.next()){
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            //Aender Bewertung
            if(text != null) {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Bewertung SET Bepunktung = ?, Text = ? WHERE BewertungID = ? AND Telefonnummer = ?;");
                preparedStatement.closeOnCompletion();
                preparedStatement.setObject(1, punktzahl);
                preparedStatement.setObject(2, text);
                preparedStatement.setObject(3, bewertungid);
                preparedStatement.setObject(4, telefonnummer);
                preparedStatement.executeUpdate();
            }
            else{
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Bewertung SET Bepunktung = ? WHERE BewertungID = ? AND Telefonnummer = ?;");
                preparedStatement.closeOnCompletion();
                preparedStatement.setObject(1, punktzahl);
                preparedStatement.setObject(2, bewertungid);
                preparedStatement.setObject(3, telefonnummer);
                preparedStatement.executeUpdate();
            }


            //return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(bewertungid)).build()).build();
            return Response.status(Response.Status.NO_CONTENT).build();

        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        finally {
            connection.close();
        }
    }

    @Path("bewertungen/{bewertungid}")
    @RolesAllowed({"KUNDE"})
    @DELETE
    public Response deleteBewertungen(@PathParam("bewertungid") Integer bewertungid)throws SQLException {
        Connection connection = dataSource.getConnection();

        try {
            if (bewertungid == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            //Finde Telefonnummer
            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT  Telefonnummer FROM Kunde WHERE Email= ?");
            preparedStatement1.closeOnCompletion();
            String kundenemail= securityContext.getUserPrincipal().getName();
            preparedStatement1.setString(1, kundenemail);
            ResultSet resultSet = preparedStatement1.executeQuery();
            Object telefonnummer = resultSet.getObject(1);

            //Not Found Abfrage
            PreparedStatement preparedStatement3 = connection.prepareStatement("SELECT * FROM Bewertung WHERE BewertungID= ?;");
            preparedStatement3.closeOnCompletion();
            preparedStatement3.setObject(1, bewertungid);
            ResultSet resultSet3 = preparedStatement3.executeQuery();

            if(!resultSet3.next()){
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            //Forbidden Abfrage
            PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT * FROM Bewertung WHERE BewertungID= ? AND Telefonnummer = ?;");
            preparedStatement2.closeOnCompletion();
            preparedStatement2.setObject(1, String.valueOf(bewertungid));
            preparedStatement2.setObject(2, telefonnummer);
            ResultSet resultSet2 = preparedStatement2.executeQuery();

            if(!resultSet2.next()){
                return Response.status(Response.Status.FORBIDDEN).build();
            }


            //Aender Bewertung
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Bewertung WHERE BewertungID = ? AND Telefonnummer = ?");
            preparedStatement.closeOnCompletion();
            preparedStatement.setObject(1, bewertungid);
            preparedStatement.setObject(2, telefonnummer);
            preparedStatement.executeUpdate();



            //eturn Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(bewertungid)).build()).build();
            return Response.status(Response.Status.NO_CONTENT).build();

        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        finally {
            connection.close();
        }
    }


    @Path("projekte/{projektid}/aufgaben")
    @RolesAllowed({"PROJEKTLEITER"})
    @POST
    public Response postAufgaben(@PathParam("projektid") Integer projektid, @FormDataParam("deadline") String deadline, @FormDataParam("beschreibung") String beschreibung, @FormDataParam("status") String status,@FormDataParam("prioritaet") String prioritaet)throws SQLException {
        Connection connection = dataSource.getConnection();

        try {
            if (projektid == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (deadline == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (status == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (prioritaet == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

//            //Finde ProjektID bzw. ob Projektleiter dem Projekt angehört
//            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT  ProjektID FROM Projekt WHERE Email= ?");
//            preparedStatement1.closeOnCompletion();
//            String kundenemail= securityContext.getUserPrincipal().getName();
//            preparedStatement1.setString(1, kundenemail);
//            ResultSet resultSet = preparedStatement1.executeQuery();
//            Object telefonnummer = resultSet.getObject(1);


            //Not Found ProjektID Abfrage
            PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT * FROM Aufgaben WHERE ProjektID= ?;");
            preparedStatement2.closeOnCompletion();
            preparedStatement2.setObject(1,projektid);
            ResultSet resultSet2 = preparedStatement2.executeQuery();

            if(!resultSet2.next()){
                return Response.status(Response.Status.NOT_FOUND).build();
            }

//            //Forbidden Abfrage
//            PreparedStatement preparedStatement3 = connection.prepareStatement("SELECT * FROM Projekt WHERE ProjektID = ? AND Email = ?;");
//            preparedStatement3.closeOnCompletion();
//            preparedStatement3.setObject(1, String.valueOf(projektid));
//            preparedStatement3.setObject(2, kundenemail);
//            ResultSet resultSet3 = preparedStatement3.executeQuery();
//
//            if(!resultSet3.next()){
//                return Response.status(Response.Status.FORBIDDEN).build();
//            }


            //Erstelle Aufgaben
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Aufgaben VALUES (?,?,?,?,?,?);");
            preparedStatement.closeOnCompletion();
            int nextid = getNextAufgabenID();
            preparedStatement.setObject(1, String.valueOf(nextid));
            preparedStatement.setObject(2, prioritaet);
            preparedStatement.setObject(3, deadline);
            preparedStatement.setObject(4, beschreibung);
            preparedStatement.setObject(5, status);
            preparedStatement.setObject(6, projektid);
            preparedStatement.executeUpdate();

            return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(nextid)).build()).build();
            //return Response.status(Response.Status.NO_CONTENT).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        finally {
            connection.close();
        }
    }

    @Path("projekte/{projektid}/spezialisten")
    @RolesAllowed({"PROJEKTLEITER"})
    @POST // POST http://localhost:8080/nutzer
    public Response postArbeitet(@PathParam("projektid") Integer projektid, @FormDataParam("spezialistid") String spezialistid)throws SQLException {
        Connection connection = dataSource.getConnection();

        try {
            if (projektid == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (spezialistid == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }


            //Not Found ProjektID Abfrage
            PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT * FROM Projekt WHERE ProjektID= ?;");
            preparedStatement2.closeOnCompletion();
            preparedStatement2.setObject(1,projektid);
            ResultSet resultSet2 = preparedStatement2.executeQuery();

            if(!resultSet2.next()){
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            //Not Found SpezialistID Abfrage
            PreparedStatement preparedStatement3 = connection.prepareStatement("SELECT * FROM Spezialist WHERE ROWID= ?;");
            preparedStatement3.closeOnCompletion();
            preparedStatement3.setObject(1,spezialistid);
            ResultSet resultSet3 = preparedStatement3.executeQuery();

            if(!resultSet3.next()){
                return Response.status(Response.Status.NOT_FOUND).build();
            }


            //Finde Email vom Spezialisten
            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT Email FROM Spezialist WHERE ROWID = ?");
            preparedStatement1.closeOnCompletion();
            preparedStatement1.setString(1, spezialistid);
            ResultSet resultSet = preparedStatement1.executeQuery();
            Object spezialistemail = resultSet.getObject(1);


            //Erstelle arbeitet
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO arbeitet VALUES (?,?);");
            preparedStatement.closeOnCompletion();
            preparedStatement.setObject(1, projektid);
            preparedStatement.setObject(2, spezialistemail);
            preparedStatement.executeUpdate();

            return Response.created(uriInfo.getAbsolutePathBuilder().path(spezialistid).build()).build();
//            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        finally {
            connection.close();
        }
    }



//Hilfsmethoden:
    private int getNextIDProgrammiersprache() throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(ROWID) FROM Programmiersprache;");
        preparedStatement.closeOnCompletion();
        ResultSet resultSet = preparedStatement.executeQuery();
        int id = 1 + resultSet.getInt(1);
        return id;
    }

    private int getNextProjektID() throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(ROWID) FROM Projekt;");
        preparedStatement.closeOnCompletion();
        ResultSet resultSet = preparedStatement.executeQuery();
        int id = 1 + resultSet.getInt(1);
        return id;
    }

    private int getNextAufgabenID() throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(ROWID) FROM Aufgaben;");
        preparedStatement.closeOnCompletion();
        ResultSet resultSet = preparedStatement.executeQuery();
        int id = 1 + resultSet.getInt(1);
        return id;
    }

    private int getNextBewertungID() throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(ROWID) FROM Bewertung;");
        preparedStatement.closeOnCompletion();
        ResultSet resultSet = preparedStatement.executeQuery();
        int id = 1 + resultSet.getInt(1);
        return id;
    }




    private int getProgrammierspracheID(String name) throws SQLException{
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT ProgrammierspracheID FROM Programmiersprache WHERE Name = ?;");
        preparedStatement.closeOnCompletion();
        preparedStatement.setObject(1,name);
        ResultSet resultSet = preparedStatement.executeQuery();
        int id = resultSet.getInt(1);
        return id;
    }



    private boolean containsProgrammiersprache(String name) throws SQLException{
        Connection connection = dataSource.getConnection();
        boolean status = true;

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT Name FROM Programmiersprache WHERE Name = ?;");
        preparedStatement.closeOnCompletion();
        preparedStatement.setObject(1,name);

        ResultSet resultSet = preparedStatement.executeQuery();

        List<Map<String, Object>> entities = new ArrayList<>();
        Map<String, Object> entity;
        while (resultSet.next()) {
            entity = new LinkedHashMap<>();
            entity.put("Name", resultSet.getObject(1));
            entities.add(entity);
        }

        if(entities.isEmpty()){
            status = false;
        }

        resultSet.close();
        connection.close();

        return status;
    }




}
