-- (1) Write MySQL query to find IPs that made more than a certain number of requests for a given time period.
-- Ex: Write SQL to find IPs that made more than 100 requests starting from 2017-01-01.13:00:00 to 2017-01-01.14:00:00.

    SELECT wsr.ip, COUNT(wsr.log_date) FROM log_database.web_server_request wsr
    WHERE wsr.log_date >= '2017-01-01.13:00:00'
    AND wsr.log_date <= '2017-01-01.14:00:00'
    GROUP BY wsr.ip
    HAVING COUNT(wsr.log_date) > 100;

-- (2) Write MySQL query to find requests made by a given IP.
--  Ex: Write SQL to find the request made by 192.168.11.231 IP.

 	SELECT * FROM log_database.web_server_request wsr
 	WHERE wsr.ip='192.168.11.231';