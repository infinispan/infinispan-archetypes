# The Infinispan Archetypes project

This project provides a set of quick-start archetypes which can be used to generate skeleton projects using [Infinispan][1].

[1]: http://www.infinispan.org

# Archetype Usage
To utilise the archetypes utilise the following command:

```
mvn archetype:generate \
    -DarchetypeGroupId=org.infinispan.archetypes \
    -DarchetypeArtifactId=<archetype-name> \
    -DarchetypeVersion=1.0.18 \
    -DarchetypeRepository=http://repository.jboss.org/nexus/content/groups/public
```

Where `<archetype-name>` can be one of the following:
  - `newproject-archetype` Creates skeleton infinispan app
  - `testcase-archetype` Creates a skeleton infinispan test cases
  - `store-archetype` Creates a skeleton implementation of a custom cache store and its configuration

