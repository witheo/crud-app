package com.aquent.crudapp.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.aquent.crudapp.person.Person;
import com.aquent.crudapp.person.PersonService;

/**
 * Controller for handling basic client management operations.
 */
@Controller
@RequestMapping("client")
public class ClientController {

    public static final String COMMAND_DELETE = "Delete";
    public static final String COMMAND_REMOVE = "Remove";

    private final ClientService clientService;
    private final PersonService personService;

    public ClientController(ClientService clientService, PersonService personService) {
        this.clientService = clientService;
        this.personService = personService;
    }

    /**
     * Renders the listing page.
     *
     * @return list view populated with the current list of people
     */
    @GetMapping(value = "list")
    public ModelAndView list() {
        ModelAndView mav = new ModelAndView("client/list");
        mav.addObject("clients", clientService.listClients());
        mav.addObject("persons", personService.listPeople());
        return mav;
    }

    /**
     * Renders an empty form used to create a new client record.
     *
     * @return create view populated with an empty client
     */
    @GetMapping(value = "create")
    public ModelAndView create() {
        ModelAndView mav = new ModelAndView("client/create");
        mav.addObject("client", new Client());
        mav.addObject("errors", new ArrayList<String>());
        return mav;
    }

    /**
     * Validates and saves a new client.
     * On success, the user is redirected to the listing page.
     * On failure, the form is redisplayed with the validation errors.
     *
     * @param client populated form bean for the client
     * @return redirect, or create view with errors
     */
    @PostMapping(value = "create")
    public ModelAndView create(Client client) {
        List<String> errors = clientService.validateClient(client);
        if (errors.isEmpty()) {
            clientService.createClient(client);
            return new ModelAndView("redirect:/client/list");
        } else {
            ModelAndView mav = new ModelAndView("client/create");
            mav.addObject("client", client);
            mav.addObject("errors", errors);
            return mav;
        }
    }

    /**
     * Renders an edit form for an existing client record.
     *
     * @param clientId the ID of the client to edit
     * @return edit view populated from the client record
     */
    @GetMapping(value = "edit/{clientId}")
    public ModelAndView edit(@PathVariable Integer clientId) {
        ModelAndView mav = new ModelAndView("client/edit");
        mav.addObject("client", clientService.readClient(clientId));
        mav.addObject("persons", personService.listPeople());
        mav.addObject("errors", new ArrayList<String>());
        return mav;
    }

    /**
     * Validates and saves an edited client.
     * On success, the user is redirected to the listing page.
     * On failure, the form is redisplayed with the validation errors.
     *
     * @param client populated form bean for the client
     * @return redirect, or edit view with errors
     */
    @PostMapping(value = "edit")
    public ModelAndView edit(Client client) {
        List<String> errors = clientService.validateClient(client);
        if (errors.isEmpty()) {
            clientService.updateClient(client);
            return new ModelAndView("redirect:/client/list");
        } else {
            ModelAndView mav = new ModelAndView("client/edit");
            mav.addObject("client", client);
            mav.addObject("errors", errors);
            return mav;
        }
    }
    
    /**
     * Renders the removeContact confirmation page.
     *
     * @param personId the ID of the personId to be removed
     * @return remove view populated from the person record
     */
    @GetMapping(value = "removeContact/{personId}")
    public ModelAndView removeContact(@PathVariable Integer personId) {
        ModelAndView mav = new ModelAndView("client/removeContact");
        mav.addObject("person", personService.readPerson(personId));
        return mav;
    }
    
    /**
     * Handles client removeContact or cancellation, redirecting to the listing page in either case.
     *
     * @param command the command field from the form
     * @param personId the ID of the person that contains the client to be removed 
     * @return redirect to the edit page
     */
    @PostMapping(value = "removeContact")
    public String removeContact(@RequestParam String command, @RequestParam Integer personId) {
    	Integer clientId = 0;
    	if (COMMAND_REMOVE.equals(command)) {
            Person person = personService.readPerson(personId);
            clientId = person.getClientId();
            person.setClientId(0);
        	personService.updatePerson(person);
        }
        return "redirect:/client/edit/" + clientId;
    }
    
    /**
     * Renders the removeContact confirmation page.
     *
     * @param personId the ID of the person to add the client id to
     * @param clientId the ID of the client to be added
     * @return redirect to the client edit page
     */
    @GetMapping(value = "addContact/{personId}/{clientId}")
    public String addContact(@PathVariable Integer personId, @PathVariable Integer clientId) {
    	Person person = personService.readPerson(personId);
        person.setClientId(clientId);
    	personService.updatePerson(person);
        return "redirect:/client/edit/{clientId}";
    }

    /**
     * Renders the deletion confirmation page.
     *
     * @param clientId the ID of the client to be deleted
     * @return delete view populated from the client record
     */
    @GetMapping(value = "delete/{clientId}")
    public ModelAndView delete(@PathVariable Integer clientId) {
        ModelAndView mav = new ModelAndView("client/delete");
        mav.addObject("client", clientService.readClient(clientId));
        return mav;
    }

    /**
     * Handles client deletion or cancellation, redirecting to the listing page in either case.
     *
     * @param command the command field from the form
     * @param clientId the ID of the client to be deleted
     * @return redirect to the listing page
     */
    @PostMapping(value = "delete")
    public String delete(@RequestParam String command, @RequestParam Integer clientId) {
        if (COMMAND_DELETE.equals(command)) {
        	List<Person> people = personService.listPeople();
        	for (Person person : people) {
        		if (person.getClientId() == clientId) {
        			person.setClientId(0);
        			personService.updatePerson(person);
        		}
        	}
            clientService.deleteClient(clientId);
        }
        return "redirect:/client/list";
    }
}
