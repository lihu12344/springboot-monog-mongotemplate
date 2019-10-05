package com.example.demo.controller;

import com.example.demo.pojo.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @RequestMapping("/save")
    public String save(){
        for(int i=0;i<100;i++){
            Person person=new Person();
            person.setId(i);
            person.setName("瓜田李下"+i);
            person.setAge(i%10+16);
            
            mongoTemplate.save(person);
        }
        
        return "success";
    }
    
    @RequestMapping("/getAll")
    public List<Person> getAll(){
        return mongoTemplate.findAll(Person.class);
    }
    
    @RequestMapping("/get")
    public List<Person> get(){
        Query query=Query.query(Criteria.where("name").regex("瓜田李下"));
        return mongoTemplate.find(query,Person.class);
    }
    
    @RequestMapping("/get2")
    public List<Person> get2(String name,Integer age){
        Criteria criteria = new Criteria();
        Criteria c1=new Criteria();
        Criteria c2=new Criteria();
        if(name!=null){
            c1=Criteria.where("name").regex(name);
        }
        
        if (age!=null){
            c2=Criteria.where("age").is(age);
        }

        criteria=criteria.andOperator(c1,c2);
        return mongoTemplate.find(Query.query(criteria),Person.class);
    }

    @RequestMapping("/get3")
    public List<Person> get3(){
        Query query=Query.query(Criteria.where("age").is(16));
        Sort sort=Sort.by("age").descending().and(Sort.by("name").ascending());
        query.with(sort);
        return mongoTemplate.find(query,Person.class);
    }

    @RequestMapping("/get4")
    public List<Person> get4(){
        PageRequest pageRequest=PageRequest.of(2,10);
        Sort sort=Sort.by("age").ascending();
        Query query=new Query().with(pageRequest).with(sort);

        return mongoTemplate.find(query,Person.class);
    }

    @RequestMapping("/update")
    public Person update(){
        Query query=Query.query(Criteria.where("age").is(18));
        Update update=new Update().set("name","海贼王").set("interest","海贼王");

        mongoTemplate.updateMulti(query,update,Person.class);
        return mongoTemplate.findOne(query,Person.class);
    }
}