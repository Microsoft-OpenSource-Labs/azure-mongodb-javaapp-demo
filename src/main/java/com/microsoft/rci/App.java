package com.microsoft.rci;
// test of new branch documentdb-test

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper; 
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class App {

	static final String mongoURI = "mongodb://rci-mongodb-demo-20161129:UhivrZpVhzWtE624p2abh49qMZmYgzx4epHMFxrtmsNOWE71CZmw63MaXziJkPi4Q4Wa4h6jd9oRmcjcWO8Mbg==@rci-mongodb-demo-20161129.documents.azure.com:10250/?ssl=true";
	public static void main(String[] args) {
		mongoOldMethods();
		mongoTestNewMethods();
	}

	public static void mongoOldMethods() {

		//MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoClientURI uri = new MongoClientURI(mongoURI);	
		MongoClient mongoClient = new MongoClient(uri);

		try {
			System.out.println("Connecting using mongoOldMethods()...");

			// Old way to get database - deprecated now
			DB db = mongoClient.getDB("radim-test1");

			DBCollection collection = db.getCollection("employee");
			System.out.println("collection: " + collection);

			System.out.println("Inserting using BasicDBObjects...");
			final BasicDBObject basic1 = new BasicDBObject();
			basic1.put("_id", "1");
			basic1.put("type", "basic");
			basic1.put("first-name", "Amaury");
			basic1.put("last-name", "Valdes");    
			collection.insert(basic1);
			System.out.println("Employee 1: " + basic1.toJson());

			final BasicDBObject basic2 = new BasicDBObject();
			basic2.put("_id", "2");
			basic2.put("type", "basic");
			basic2.put("first-name", "Jane");
			basic2.put("last-name", "Valdes");    
			collection.insert(basic2);
			System.out.println("Employee 2: " + basic2.toJson());

			showAllDocs(collection);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			mongoClient.close();
		}
	}

	public static void mongoTestNewMethods() {    

		MongoClientURI uri = new MongoClientURI(mongoURI);	
		MongoClient mongoClient = new MongoClient(uri);

		//MongoClient mongoClient = new MongoClient("localhost", 27017);

		try {
			System.out.println(
					"Connecting using mongoTestNewMethods() to 'radim-test1' database...");
			MongoDatabase db = mongoClient.getDatabase("radim-test1");

			MongoCollection<Document> collection = db.getCollection("employee");
			System.out.println("Inserting using Map...");

			//---Insert using Map Employee #1---
			final Map<String,Object> empMap1 = new HashMap<String, Object>();
			empMap1.put("_id", "101");
			empMap1.put("type", "map");
			empMap1.put("first-name", "Stephen");
			empMap1.put("last-name", "Murphy");

			System.out.println("Employee: 101" + empMap1);
			collection.insertOne(new Document(empMap1));

			//---Insert using Map Employee #2---
			final Map<String,Object> empMap2 = new HashMap<String, Object>();
			empMap2.put("_id", "102");
			empMap2.put("type", "map");
			empMap2.put("first-name", "Harold");
			empMap2.put("last-name", "Jansen");

			System.out.println("Employee: 102" + empMap2);
			collection.insertOne(new Document(empMap2));

			//Show all documents in the collection
			showAllDocuments(collection);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mongoClient.close();
		}
	}

	public static void showAllDocuments( 
			final MongoCollection<Document> collection) {

		System.out.println("----[Retrieve All Documents in Collection]----");
		for (Document doc: collection.find()) {
			System.out.println(doc.toJson());
		}
	}

	public static void showAllDocs(final DBCollection collection) {
		DBCursor cursor = (DBCursor) collection.find().iterator();
		try {
			while (cursor.hasNext()) {
				System.out.println(cursor.next().toString());
			}
		} finally {
			cursor.close();
		}
	}
}