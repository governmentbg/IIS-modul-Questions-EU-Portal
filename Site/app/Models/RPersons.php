<?php

namespace App\Models;

use App\ANsAssembly;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $person_id
 * @property string     $name1
 * @property string     $name2
 * @property string     $name3
 * @property int        $pos_id
 * @property int        $instit_id
 * @property int        $ns_MPid
 * @property Date       $date1
 * @property Date       $date2
 * @property int        $A_id
 */
class RPersons extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'R_persons';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'person_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'person_id', 'name1', 'name2', 'name3', 'pos_id', 'instit_id', 'ns_MPid', 'date1', 'date2', 'arh', 'A_id'
    ];

    /**
     * The attributes excluded from the model's JSON form.
     *
     * @var array
     */
    protected $hidden = [];

    /**
     * The attributes that should be casted to native types.
     *
     * @var array
     */
    protected $casts = [
        'person_id' => 'int', 'name1' => 'string', 'name2' => 'string', 'name3' => 'string', 'pos_id' => 'int', 'instit_id' => 'int', 'ns_MPid' => 'int', 'date1' => 'date', 'date2' => 'date', 'A_ns_id' => 'int'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'date1', 'date2'
    ];

    /**
     * Indicates if the model should be timestamped.
     *
     * @var boolean
     */
    public $timestamps = false;

    // Scopes...

    // Functions ...

    // Relations ...
    public function eq_position()
    {
        return $this->belongsTo(RPosition::class, 'pos_id');
    }
    public function eq_inst()
    {
        return $this->belongsTo(RInstitution::class, 'instit_id');
    }
    public function eq_assembly()
    {
        return $this->belongsTo(ANsAssembly::class, 'A_ns_id');
    }
    public function eq_file()
    {
        return $this->hasMany(RDeclFile::class, 'person_id');
    }
}
