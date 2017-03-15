/*
 * © Copyright IBM Corp. 2017
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package client.mail.test;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.MessageApi;
import io.swagger.client.model.Message;
import io.swagger.client.model.MessagePart;
import io.swagger.client.model.Person;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MessageTest {
    
    private static final String MIME_BOUNDARY = "abcdefg";

    private MessageApi _api;

    public MessageTest(String basePath, String user, String password) {
        _api = new MessageApi();
        ApiClient client = _api.getApiClient();
        client.setBasePath(basePath);
        client.setUsername(user);
        client.setPassword(password);
    }

    public static void main(String[] args) {

        // IMPORTANT: Change these values to match your test environment:
        //
        // - basePath is the base URL for your Domino server.
        //   The mail service MUST be installed and enabled
        //   on the server. Be sure to change the protocol to
        //   https if necessary.
        //
        // - folder is mail file database folder.
        //
        // - database is the mail file database name.
        //
        // - user is the user name of the owner of the mail file specified by
        //   /{folder}/{database}.
        //
        // - password is the user's password.
		//
		// - sendTo is the email address of a recipient
        //
        String basePath = "http://yourserver.yourorg.com";
        String folder = "mail";
        String database = "database.nsf";
        String user = "First Last";
        String password = "password";
        String sendTo = "recipient@yourorg.com";

        // Create an instance of the test class
        
        MessageTest test = new MessageTest(basePath, user, password);
        
        // Create a message
        
        Message message = new Message();
        message.setSubject("Test message from Swagger generated Java client code");
        
        Person person = new Person();
        person.setEmail(sendTo);
        message.addToItem(person);
        
        MessagePart part = new MessagePart();
        part.setContentType("multipart/alternative; Boundary=" + MIME_BOUNDARY);
        message.addContentItem(part);
        
        part = new MessagePart();
        part.setContentType("text/plain");
        part.setBoundary("--" + MIME_BOUNDARY);
        part.setData("This is a test.");
        message.addContentItem(part);

        part = new MessagePart();
        part.setContentType("text/html");
        part.setBoundary("--" + MIME_BOUNDARY);
        part.setData("<html><body>This is a test.<p><b>This is only a test!</b></body></html>");
        message.addContentItem(part);

        // Send the message
        
        String unid = test.sendMessage(folder, database, message);
        
        // Read the message

        if ( unid != null ) {
            test.readMessage(folder, database, unid);
        }
    }

    /**
     * Sends a message.
     * 
     * @param folder
     * @param database
     */
    private String sendMessage(String folder, String database, Message message) {
        String unid = null;

        try {

            // Send the message
            System.out.println("Sending message from " + folder + "/" + database + " ...");
            ApiResponse<Void> result = _api.folderDatabaseApiMailOutboxPostWithHttpInfo(folder, database, message);
            
            // Extract the Location header
            String location = null;
            Map<String, List<String>> headers = result.getHeaders();
            if (headers != null) {
                Set<String> keys = headers.keySet();
                for (String key : keys) {
                    if (key.equalsIgnoreCase("Location")) {
                        location = headers.get(key).get(0);
                        break;
                    }
                }
            }

            // Extract the UID from the location
            if (location != null) {
                // Dump the Location header
                System.out.println("Send message request succeeded.  Location is " + location);

                int index = location.lastIndexOf('/');
                if (index != -1) {
                    unid = location.substring(index + 1, location.length());
                }
            }
            else {
                // Unexpected response
                System.out.println("Send message request succeeded, but the response doesn't include a Location header.");
            }

            System.out.println();
        }
        catch (ApiException e) {
            System.err.println("Exception when calling MessageApi#folderDatabaseApiMailOutboxPostWithHttpInfo");
            String body = e.getResponseBody();
            if (body != null) {
                System.err.println("Response from server ...");
                System.err.println(body);
            }
            else {
                e.printStackTrace();
            }
        }
        
        return unid;
    }
    
    /**
     * Reads a message and dumps its contents.
     * 
     * @param folder
     * @param database
     * @param unid
     */
    private void readMessage(String folder, String database, String unid) {
        
        try {

            // Send the message
            System.out.println("Reading message " + unid + " from " + folder + "/" + database + " ...");
            Message message = _api.folderDatabaseApiMailMessagesUnidGet(folder, database, unid, null, null);
            
            if ( message != null ) {
                
                // Successful response
                System.out.println("Read message request succeeded.  Response follows ...");
                
                System.out.println("  subject: " + message.getSubject());
                System.out.println("  date: " + message.getDate());
                System.out.println();
                
                List<MessagePart> parts = message.getContent();
                for ( MessagePart part : parts ) {
                    String contentType = part.getContentType();
                    if ( contentType != null && contentType.startsWith("text") ) {
                        System.out.println("  contentType: " + contentType);
                        System.out.println("  data: " + part.getData());
                        System.out.println();
                    }
                }
            }
            else {
                // Unexpected response
                System.out.println("Read message request succeeded, but the response doesn't include a message.");
            }
        }
        catch (ApiException e) {
            System.err.println("Exception when calling MessageApi#folderDatabaseApiMailMessagesUnidGet");
            String body = e.getResponseBody();
            if (body != null) {
                System.err.println("Response from server ...");
                System.err.println(body);
            }
            else {
                e.printStackTrace();
            }
        }
    }

}
