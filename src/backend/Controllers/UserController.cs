using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using backend.ModelsDto;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace backend.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    [Authorize(Roles = "korisnik")]
    public class UserController : ControllerBase
    {
        private readonly DataContext _context;
        private readonly IConfiguration _configuration;

        public UserController(DataContext context,IConfiguration configuration)
        {
            _context = context;
            _configuration = configuration;
        }

        [HttpGet("profileInfo")]
        public async Task<ActionResult<UserProfileDto>> getProfileInfo()
        {
            var user = await _context.Users.FirstOrDefaultAsync(u => u.Username == User.Identity.Name);
            UserProfileDto upd = new UserProfileDto
            {
                Username = user.Username,
                Name = user.Name,
                Description = user.Description,
                Avatar = user.Avatar
            };
            return Ok(upd);
        }
    }
}
