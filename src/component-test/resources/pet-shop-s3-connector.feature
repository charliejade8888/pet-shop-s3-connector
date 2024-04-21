Feature: Pet Shop S3 Connector
  Scenario: fetch a pre-signed link to upload a file
    Given I need a pre-signed link to "upload" a file "my.txt" using bucket "bucket"
    When I make a request for the link
    Then the pre-signed link should be successfully returned

  Scenario: fetch a pre-signed link to download a file
    Given I need a pre-signed link to "download" a file "my.txt" using bucket "bucket"
    When I make a request for the link
    Then the pre-signed link should be successfully returned

  Scenario: using the pre-signed links
    Given I need a pre-signed link to "upload" a file "my.txt" using bucket "bucket"
    And I make a request for the link
    And I use the link to "upload" a file "my.txt" into a bucket "bucket"
    And I need a pre-signed link to "download" a file "my.txt" using bucket "bucket"
    And I make a request for the link
    When I use the link to "download" a file "my.txt" into a bucket "bucket"
    Then the file is present
#    TODO then for verify file contents / check file exists and has content as expected (pass in string above)
#  TODO lump some of these together !
