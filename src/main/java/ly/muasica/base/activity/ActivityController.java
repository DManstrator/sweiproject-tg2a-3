package ly.muasica.base.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ly.muasica.base.activity.structure.Tag;
import ly.muasica.base.activity.structure.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/activity")
public class ActivityController {
  
  @Autowired
  private ActivityRepository activityRepository;
  
  @Autowired
  private TagRepository tagRepository;
  
  
  @GetMapping
  public ArrayList<Activity> listAll() {
      ArrayList<Activity> activities = new ArrayList<>();
      activityRepository.findAll().forEach(activity -> activities.add(activity));
      return activities;
  }

  @GetMapping("{id}")
  public Activity find(@PathVariable Long id) {
      return activityRepository.findOne(id);
  }

  @PostMapping
  public Activity create(@RequestBody Activity input) {
      ArrayList<Tag> tags = new ArrayList<>();
      input.getTags().stream().forEach(tag -> {
    	  Tag newTag = new Tag(tag.getName());
//    	  tagRepository.save(newTag); // TODO DB Crash, too big to be inserted
          tags.add(newTag);
          });
      System.out.println(tags);
      return activityRepository.save(new Activity(input.getText(), input.getTitle(), tags));
  }

  @DeleteMapping("{id}")
  public void delete(@PathVariable Long id) {
      activityRepository.delete(id);
  }

  @PutMapping("{id}")
  public Activity update(@PathVariable Long id, @RequestBody Activity input) {
      Activity activity = activityRepository.findOne(id);
      if (activity == null) {
          return null;
      } else {
          activity.setText(input.getText());
          activity.setTitle(input.getTitle());
          activity.setTags(input.getTags());
          return activityRepository.save(activity);
      }
  }
  
  @GetMapping("/filter/{name}")
  public List<Activity> filter(@PathVariable String name)  {
      ArrayList<Activity> activities = listAll();

      List<Activity> filtered = activities
              .stream().filter(activity -> activity.getTags().stream()
                      .anyMatch(user -> user.getName().equals(name))
                      )
              .collect(Collectors.toList());
      return filtered;
  }

}
