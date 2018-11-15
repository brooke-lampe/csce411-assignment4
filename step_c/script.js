
var result = d3.json("input_graph.json", function(data) {
    var links = data.links.map(d => Object.create(d));
    var nodes = data.nodes.map(d => Object.create(d));
    renderGraph(nodes, links);
});

function renderGraph(nodes, links) {
    var width = 800, height = 800;
    var force = d3.layout.force()
      .charge(-200)
      .linkDistance(30)
      .size([width, height]);
  
    var svg = d3.select("#graph").append("svg")
      .attr("width", "100%").attr("height", "100%")
      .attr("pointer-events", "all");

    force.nodes(nodes).links(links).start();
  
    var link = svg.selectAll(".link")
      .data(links).enter()
      .append("line").attr("class", "link");
  
    var node = svg.selectAll(".node")
      .data(nodes).enter()
      .append("circle")
      .attr("class", d => { return "node " + d.label })
      .attr("r", 5)
      .call(force.drag);

    node.append("title").text(d => { return d.source; });
    
    node.on("mouseover", function(d) {
      node.style("fill", (o) => {
        if (d == o) return "magenta";
      });
      node.style("stroke", (o) => {
        if (d == o) return "magenta";
      });
    });

    node.on("mouseout", function(d, i) {
      node.style("stroke", "#222");
      node.style("fill", "#222");
    });

    force.on("tick", () => {
      link.attr("x1", d => { return d.source.x; })
          .attr("y1", d => { return d.source.y; })
          .attr("x2", d => { return d.target.x; })
          .attr("y2", d => { return d.target.y; });
  
      node.attr("cx", d => { return d.x })
          .attr("cy", d => { return d.y;});
    });

  }