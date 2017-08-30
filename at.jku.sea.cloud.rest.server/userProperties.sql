SELECT	u.id as userid, u.password,
	a.id as artifactid, a.version as artifactversion,
	p.version as propertyversion, p.name as propertyname, p.value as propertyvalue
FROM
	users u
JOIN
	artifacts a 
ON		a.id = u.owner
JOIN
	properties p
ON		p.artifactid = a.id